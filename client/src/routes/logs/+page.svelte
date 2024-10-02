<script lang="ts">
	import AnnotatedCard from '$lib/components/logs/AnnotatedCard.svelte';
	import type { PageData } from '../$types';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import Accordion, { Panel, Header, Content } from '@smui-extra/accordion';
	import IconButton, { Icon } from '@smui/icon-button';
	import { formatDate, imageUrl } from '$lib/util';
	import type { AnnotationWithPosture } from '$api/generated';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';

	export let data: PageData;
	const base = import.meta.env.VITE_BASE_PATH;
	type Content = AnnotationWithPosture['annotation'] & {
		originalNeckAngle: number;
		diffNeckAngle: number;
		userId: number;
		fileName: string;
	};
	type Key = keyof Content;
	type Data = {
		prod: Content[];
		sample: Content[];
	};
	type Count = {
		display: number;
		total: number;
	};

	const formatData = (d: AnnotationWithPosture): Content => {
		return {
			...d.annotation,
			originalNeckAngle: d.posture.neckAngle,
			diffNeckAngle: d.annotation.neckAngle - d.posture.neckAngle,
			userId: d.posture.userId,
			fileName: formatDate(d.posture.exCreatedAt) + '.jpg'
		};
	};

	const contents =
		data.prod && data.sample
			? [
					{
						title: 'サンプルデータ',
						data: data.sample.annotations.map(formatData),
						open: data.sample.count === 0,
						subPath: 'sample' as 'sample',
						key: 'sample' as keyof Data
					},
					{
						title: '本番データ',
						data: data.prod.annotations.map(formatData),
						open: data.prod.count === 0,
						key: 'prod' as keyof Data
					}
				]
			: [];
	const optionTemplate: Option<Key>[] = [
		{ key: 'id', label: 'ID', type: 'number', availableUiTypes: ['dropdown'] },
		{ key: 'neckAngle', label: '首の角度', type: 'number', availableUiTypes: ['dropdown'] },
		{
			key: 'originalNeckAngle',
			label: 'もとの首の角度',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{ key: 'diffNeckAngle', label: '首の角度の差', type: 'number', availableUiTypes: ['dropdown'] },
		{ key: 'userId', label: 'ユーザーID', type: 'number', availableUiTypes: ['dropdown'] },
		{ key: 'createdAt', label: '作成日時', type: 'date', availableUiTypes: ['dropdown'] }
	];

	let formattedData: Data = {
		prod: [...data.prod.annotations.map(formatData)],
		sample: [...data.sample.annotations.map(formatData)]
	};

	let filteredData: Data = { ...formattedData };

	$: counts = {
		prod: { display: filteredData.prod.length, total: data.prod.count },
		sample: { display: filteredData.sample.length, total: data.sample.count }
	} as { [key in keyof Data]: Count };
</script>

<div class="wrapper">
	<h1>アノテーション履歴</h1>
	<Accordion multiple>
		{#each contents as content}
			<Panel bind:open={content.open}>
				<Header>
					{content.title}（{content.data.length}件）
					<IconButton slot="icon" toggle pressed={content.open}>
						<Icon class="material-icons" on>unfold_less</Icon>
						<Icon class="material-icons">unfold_more</Icon>
					</IconButton>
				</Header>
				<Content>
					{#if content.data.length === 0}
						<Content>
							<p>データがありません</p>
						</Content>
					{:else}
						<DataSortFilter
							{optionTemplate}
							bind:data={formattedData[content.key]}
							bind:counts={counts[content.key]}
							on:updateData={(e) => (filteredData[content.key] = e.detail)}
						/>
						<LayoutGrid>
							{#each filteredData[content.key] as annotation}
								<Cell
									class="card mdc-elevation-transition"
									spanDevices={{ desktop: 2, tablet: 2, phone: 4 }}
								>
									<AnnotatedCard
										{annotation}
										originalNeckAngle={annotation.originalNeckAngle}
										navigatePath={`${base}/annotate${content.subPath ? `/${content.subPath}` : ''}`}
										imageSrc={imageUrl(
											{
												userId: content.subPath ?? annotation.userId,
												annotaterId: data.user?.id,
												fileName: annotation.fileName
											},
											'annotated'
										)}
									/>
								</Cell>
							{/each}
						</LayoutGrid>
					{/if}
				</Content>
			</Panel>
		{/each}
	</Accordion>
</div>

<style lang="scss" scoped>
	@import '$lib/styles/variables.scss';

	.wrapper {
		text-align: center;
		height: fit-content;
		margin-bottom: 12px;

		:global(.smui-accordion__panel) {
			margin-bottom: 24px;

			:global(.smui-accordion__header) {
				background-color: $base-color;
			}

			:global(.smui-paper__content) {
				background-color: grayscale($color: $base-color);
			}
		}

		h1 {
			text-align: center;
		}

		.card {
			margin: auto;
			width: 80%;
		}
	}
</style>
