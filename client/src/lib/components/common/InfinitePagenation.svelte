<script lang="ts">
	import { createEventDispatcher } from 'svelte';
	import { Label, Pagination } from '@smui/data-table';
	import IconButton from '@smui/icon-button';
	import Select, { Option } from '@smui/select';

	type T = $$Generic;
	export let contents: T[];
	export let displayData: T[];
	export let isLast: boolean;

	const dispatch = createEventDispatcher<{
		loadMore: { page: number; size: number; refresh: boolean };
	}>();

	let currentPage = 0;
	export let rowsPerPage = 50;

	$: start = currentPage * rowsPerPage;
	$: end = Math.min(start + rowsPerPage, contents.length);
	$: lastPage = Math.ceil(contents.length / rowsPerPage) - (isLast ? 1 : 0);
	const loadMore = (page: number, size: number, refresh: boolean) => {
		if (page < 0 || page > lastPage) return;

		dispatch('loadMore', { page: page, size: size, refresh });
	};

	$: if (contents.length < currentPage * rowsPerPage) {
		currentPage--;
	}

	$: if (rowsPerPage > contents.length && !isLast) {
		currentPage = 0;
		loadMore(0, rowsPerPage, true);
	}

	$: {
		displayData = contents.slice(start, end);
	}
</script>

<div class="infinit-pagenation-container">
	<Pagination slot="paginate">
		<svelte:fragment slot="rowsPerPage">
			<Label>Rows Per Page</Label>
			<Select variant="outlined" bind:value={rowsPerPage} noLabel>
				<Option value={50}>50</Option>
				<Option value={100}>100</Option>
				<Option value={200}>200</Option>
				<Option value={500}>500</Option>
				<Option value={1000}>1000</Option>
			</Select>
		</svelte:fragment>
		<svelte:fragment slot="total">
			{start + 1}-{end} of {contents.length}
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
			on:click={() => {
				loadMore(++currentPage, rowsPerPage, false);
			}}
			disabled={currentPage === lastPage}>chevron_right</IconButton
		>
		<IconButton
			class="material-icons"
			action="last-page"
			title="Last page"
			on:click={() => {
				for (let i = currentPage; i < lastPage; i++) {
					loadMore(i, rowsPerPage, false);
				}
				currentPage = lastPage;
			}}
			disabled={currentPage === lastPage}>last_page</IconButton
		>
	</Pagination>
</div>

<style lang="scss" scoped>
	.infinit-pagenation-container {
		:global(.mdc-data-table__pagination) {
			border: none;
		}
	}
</style>
