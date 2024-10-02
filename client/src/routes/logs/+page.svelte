<script lang="ts">
	import AnnotatedCard from '$lib/components/logs/AnnotatedCard.svelte';
	import type { PageData } from '../$types';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import Accordion, { Panel, Header, Content } from '@smui-extra/accordion';
	import IconButton, { Icon } from '@smui/icon-button';
	import { formatDate, imageUrl } from '$lib/util';
	import type { AnnotationWithPosture } from '$api/generated';

	export let data: PageData;
	const base = import.meta.env.VITE_BASE_PATH;

	const formatData = (d: AnnotationWithPosture) => {
		return {
			...d.annotation,
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
						subPath: 'sample'
					},
					{
						title: '本番データ',
						data: data.prod.annotations.map(formatData),
						open: data.prod.count === 0
					}
				]
			: [];
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
						<LayoutGrid>
							{#each content.data as annotation}
								<Cell
									class="card mdc-elevation-transition"
									spanDevices={{ desktop: 2, tablet: 2, phone: 4 }}
								>
									<AnnotatedCard
										{annotation}
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
				// background-color: $secondary-color;
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
