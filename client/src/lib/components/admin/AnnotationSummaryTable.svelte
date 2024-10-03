<script lang="ts">
	import type { Header } from './types/AnnotationSummaryTable';

	import DataTable, { Head, Body, Row, Cell } from '@smui/data-table';
	import ToggleImage from '../common/ToggleImage.svelte';
	import { getHighlightColor, imageUrlFromPath } from '$lib/util';

	type T = $$Generic<Recorc<string, number | string>>;
	type PARAM = $$Generic;
	export let data: T[];
	export let headers: Header<keyof T>[];
	export let navigateToDetail: (row: T) => void;
</script>

{#if !data}
	<p>データがありません</p>
{:else}
	<DataTable stickyHeader>
		<Head>
			<Row>
				{#each headers as header}
					<Cell>{header.display}</Cell>
				{/each}
			</Row>
		</Head>
		<Body>
			{#each data as stats}
				<Row>
					{#each headers as head}
						<Cell
							numeric={head.type === 'number'}
							class={getHighlightColor(stats[head.key], head.highlightThreshold)}
							on:click={head.clickable === false ? undefined : () => navigateToDetail(stats)}
						>
							{#if head.type === 'image'}
								<ToggleImage
									src={imageUrlFromPath(`original/${stats.fileName}`)}
									width={150}
									viewBox={{ top: 40, right: 35, bottom: 35, left: 35 }}
								/>
							{:else if head.type === 'number' && head.digit}
								{stats[head.key].toFixed(head.digit)}
							{:else}
								{stats[head.key]}
							{/if}
						</Cell>
					{/each}
				</Row>
			{/each}
		</Body>
	</DataTable>
{/if}

<style lang="scss" scoped>
	@import '$lib/styles/_variables';
	:global(.mdc-data-table) {
		:global(.mdc-data-table__cell.green) {
			background-color: $highlight-green;
		}

		:global(.mdc-data-table__cell.red) {
			background-color: $highlight-red;
		}
	}
</style>
