<script lang="ts">
	import AnnotatedCard from '$lib/components/logs/AnnotatedCard.svelte';
	import type { PageData } from '../$types';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import Accordion, { Panel, Header, Content } from '@smui-extra/accordion';
	import IconButton, { Icon } from '@smui/icon-button';
	import { formatDate, imageUrl, mergeArray } from '$lib/util';
	import type { AnnotationWithPosture } from '$api/generated';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import InfinitePagenation from '$lib/components/common/InfinitePagenation.svelte';
	import { createAnnotationApi } from '$api';

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
	type Kind = Omit<typeof data, 'user'>;

	const formatData = (d: AnnotationWithPosture): Content => {
		return {
			...d.annotation,
			originalNeckAngle: d.posture.neckAngle,
			diffNeckAngle: d.annotation.neckAngle - d.posture.neckAngle,
			userId: d.posture.userId,
			fileName: formatDate(d.posture.exCreatedAt) + '.jpg'
		};
	};
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

	const loadFn = async (param: { page: number; size: number }, kind: Kind) => {
		const api = createAnnotationApi({
			basePath: import.meta.env.VITE_API_CLIENT_URL,
			token: data?.user?.token || ''
		});
		const annotaterId = data.user?.id || 0;
		if (kind === 'prod') {
			const prods = await api.getProdAnnotationsWithPostureByAnnotaterId({ annotaterId, ...param });
			return prods;
		} else if (kind === 'sample') {
			const samples = await api.getSampleAnnotationsWithPostureByAnnotaterId({
				annotaterId,
				...param
			});
			return samples;
		}
	};
	const loadMore = async (
		{
			page,
			size,
			refresh
		}: {
			page: number;
			size: number;
			refresh: boolean;
		},
		kind: Kind
	) => {
		if (!data[kind]) {
			const res = await loadFn({ page: 0, size }, kind);
			data = { ...data, [kind]: { ...data[kind], annotations: res } };
			return;
		}
		if (
			page > data[kind].annotations.totalPages ||
			data[kind].annotations.isLast ||
			(page + 1) * size <= displayData[kind].length
		) {
			return;
		}
		try {
			const _page = refresh ? 0 : Math.ceil(data[kind].annotations.contents.length / size);
			const res = await loadFn({ page: _page, size }, kind);
			data[kind].annotations = refresh
				? res
				: {
						...data[kind].annotations,
						...res,
						contents: mergeArray(
							data[kind].annotations.contents,
							res?.contents || [],
							(item: AnnotationWithPosture) => item.annotation.id
						)
					};
		} catch (e) {
			console.error(e);
		}
	};

	const mutateFilteredData = (d: Data, kind: Kind) => {
		// console.log(d);
		filteredData[kind] = d;
	};

	let formattedData: Data = {
		prod: [...data.prod.annotations.contents.map(formatData)],
		sample: [...data.sample.annotations.contents.map(formatData)]
	};

	let filteredData: Data = { ...formattedData };

	let displayData: Data = { ...filteredData };

	$: counts = {
		prod: { display: filteredData.prod.length, total: data.prod.count },
		sample: { display: filteredData.sample.length, total: data.sample.count }
	} as { [key in keyof Data]: Count };

	const contents =
		data.prod && data.sample
			? [
					{
						title: 'サンプルデータ',
						open: data.sample.count === 0,
						subPath: 'sample' as 'sample',
						count: data.sample.count,
						kind: 'sample' as Kind
					},
					{
						title: '本番データ',
						open: data.prod.count === 0,
						count: data.prod.count,
						kind: 'prod' as Kind
					}
				]
			: [];

	$: {
		if (
			data.prod?.annotations?.contents.length !== formattedData.prod.length ||
			data.sample?.annotations?.contents.length !== formattedData.sample.length ||
			formattedData.prod.length !== filteredData.prod.length ||
			formattedData.sample.length !== filteredData.sample.length
		) {
			formattedData = {
				prod: [...data.prod.annotations.contents.map(formatData)],
				sample: [...data.sample.annotations.contents.map(formatData)]
			};
			filteredData = { ...formattedData };
			displayData = { ...filteredData };
		}
	}
</script>

<div class="wrapper">
	<h1>アノテーション履歴</h1>
	<Accordion multiple>
		{#each contents as content}
			<Panel bind:open={content.open}>
				<Header>
					{content.title}（{data[content.kind]?.count || 0}件）
					<IconButton slot="icon" toggle pressed={content.open}>
						<Icon class="material-icons" on>unfold_less</Icon>
						<Icon class="material-icons">unfold_more</Icon>
					</IconButton>
				</Header>
				<Content>
					{#if (formattedData?.[content.kind]?.length || 0) === 0}
						<Content>
							<p>データがありません</p>
						</Content>
					{:else}
						<DataSortFilter
							{optionTemplate}
							bind:data={formattedData[content.kind]}
							bind:counts={counts[content.kind]}
							on:updateData={(e) => mutateFilteredData(e.detail, content.kind)}
							id={`DataSortFilter_/logs/${content.kind}`}
						/>
						<LayoutGrid>
							{#each displayData[content.kind] as annotation}
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
						<InfinitePagenation
							bind:contents={filteredData[content.kind]}
							bind:displayData={displayData[content.kind]}
							bind:isLast={data[content.kind].annotations.isLast}
							on:loadMore={({ detail }) => loadMore(detail, content.kind)}
							id={`InfinitePagenation_/logs/${content.kind}`}
						/>
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
