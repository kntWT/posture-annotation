<script lang="ts">
	import DataTable, { Head, Body, Row, Cell, Pagination } from '@smui/data-table';
	import Select, { Option } from '@smui/select';
	import IconButton from '@smui/icon-button';
	import { Label } from '@smui/common';
	import type { AnnotationSummary } from '$api/generated';

	export let data: AnnotationSummary[];
	export let navigateToDetail: (id: number) => void;

	type color = 'green' | 'red' | '';
	let currentPage = 0;
	let rowsPerPage = 50;
	$: start = currentPage * rowsPerPage;
	$: end = Math.min(start + rowsPerPage, data.length);
	$: slice = data.slice(start, end);
	$: lastPage = Math.max(Math.ceil(data.length / rowsPerPage) - 1, 0);
	$: if (currentPage > lastPage) {
		currentPage = lastPage;
	}

	const getHighlight = (value: number, thres?: number): color => {
		if (!thres) {
			return '';
		}
		if (value > thres) {
			return 'green';
		} else if (value < -thres) {
			return 'red';
		} else {
			return '';
		}
	};
</script>

{#if !data}
	<p>データがありません</p>
{:else}
	<DataTable stickyHeader>
		<Head>
			<Row>
				<Cell>id</Cell>
				<Cell>アノテーション数</Cell>
				<Cell>もとの首の角度</Cell>
				<Cell>角度の平均</Cell>
				<Cell>角度の標準偏差</Cell>
				<Cell>角度の平均の差</Cell>
				<Cell>アノテータ</Cell>
			</Row>
		</Head>
		<Body>
			{#each slice as stats}
				<Row on:click={() => navigateToDetail(stats.postureId)}>
					<Cell>#{stats.postureId}</Cell>
					<Cell numeric>{stats.annotationIds.length}</Cell>
					<Cell numeric>{stats.originalNeckAngle}</Cell>
					<Cell numeric>{stats.avgNeckAngle}</Cell>
					<Cell numeric class={getHighlight(stats.stdNeckAngle, 10)}>{stats.stdNeckAngle}</Cell>
					<Cell numeric class={getHighlight(stats.avgNeckAngle - stats.originalNeckAngle, 10)}
						>{stats.avgNeckAngle - stats.originalNeckAngle}</Cell
					>
					<Cell numeric>{stats.annotaterIds.toSorted()}</Cell>
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
	:global(.mdc-data-table) {
		:global(.mdc-data-table__cell.green) {
			background-color: #66bb6a;
		}
		:global(.mdc-data-table__cell.red) {
			background-color: #ef5350;
		}
	}
</style>
