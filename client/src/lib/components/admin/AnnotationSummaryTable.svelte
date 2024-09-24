<script lang="ts">
	import type { Header } from './types/AnnotationSummaryTable';

	import DataTable, { Head, Body, Row, Cell, Pagination } from '@smui/data-table';
	import Select, { Option } from '@smui/select';
	import IconButton from '@smui/icon-button';
	import { Label } from '@smui/common';
	import ToggleImage from '../common/ToggleImage.svelte';
	import { getHighlightColor, imageUrlFromPath } from '$lib/util';

	type T = $$Generic<Recorc<string, number | string>>;
	export let data: T[];
	export let headers: Header<keyof T>[];
	export let navigateToDetail: (row: T) => void;

	let currentPage = 0;
	let rowsPerPage = 50;
	$: start = currentPage * rowsPerPage;
	$: end = Math.min(start + rowsPerPage, data.length);
	$: slice = data.slice(start, end);
	$: lastPage = Math.max(Math.ceil(data.length / rowsPerPage) - 1, 0);
	$: if (currentPage > lastPage) {
		currentPage = lastPage;
	}
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
			{#each slice as stats}
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
									viewBox={{ top: 950, right: 550, bottom: 700, left: 550 }}
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

		<Pagination slot="paginate">
			<svelte:fragment slot="rowsPerPage">
				<Label>Rows Per Page</Label>
				<Select variant="outlined" bind:value={rowsPerPage} noLabel>
					<Option value={50}>50</Option>
					<Option value={100}>100</Option>
					<Option value={200}>200</Option>
				</Select>
			</svelte:fragment>
			<svelte:fragment slot="total">
				{start + 1}-{end} of {data.length}
			</svelte:fragment>

			<IconButton
				class="material-icons"
				action="first-page"
				title="First page"
				on:click={() => (currentPage = 0)}
				disabled={currentPage === 0}>first_page</IconButton
			>
			<IconButton
				class="material-icons"
				action="prev-page"
				title="Prev page"
				on:click={() => currentPage--}
				disabled={currentPage === 0}>chevron_left</IconButton
			>
			<IconButton
				class="material-icons"
				action="next-page"
				title="Next page"
				on:click={() => currentPage++}
				disabled={currentPage === lastPage}>chevron_right</IconButton
			>
			<IconButton
				class="material-icons"
				action="last-page"
				title="Last page"
				on:click={() => (currentPage = lastPage)}
				disabled={currentPage === lastPage}>last_page</IconButton
			>
		</Pagination>
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
