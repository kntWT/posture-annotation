<script lang="ts">
	import AnnotationSummaryTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummaryByAnnotater } from '$api/generated';
	import { goto } from '$app/navigation';
	import type { PageData } from './$types';
	import type { Header } from '$lib/components/admin/types/AnnotationSummaryTable';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import InfinitePagenation from '$lib/components/common/InfinitePagenation.svelte';
	import { createAnnotationApi } from '$api';
	export let data: PageData;

	type Key = keyof AnnotationSummaryByAnnotater;

	const navigateToDetail = (data: AnnotationSummaryByAnnotater) => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/user/detail?annotater_id=${data.annotaterId}`, {
			invalidateAll: true
		});
	};
	const headers: Header<Key>[] = [
		{
			display: 'id',
			key: 'annotaterId',
			type: 'number'
		},
		{
			display: 'ユーザ名',
			key: 'name',
			type: 'string'
		},
		{
			display: '件数',
			key: 'count',
			type: 'number'
		},
		{
			display: '差分の平均',
			key: 'avgDiffOriginalNeckAngle',
			type: 'number',
			digit: 4,
			highlightThreshold: 20
		},
		{
			display: '差分の平均の平均',
			key: 'avgDiffAvgNeckAngle',
			type: 'number',
			digit: 4,
			highlightThreshold: 10
		},
		{
			display: '差分の平均の標準偏差',
			key: 'stdDiffAvgNeckAngle',
			type: 'number',
			digit: 4,
			highlightThreshold: 10
		}
	];

	const optionTemplate: Option<Key>[] = [
		{ label: 'id', key: 'annotaterId', type: 'number', availableUiTypes: ['dropdown'] },
		{ label: '件数', key: 'count', type: 'number', availableUiTypes: ['dropdown'] },
		{ label: 'ユーザ名', key: 'name', type: 'string', availableUiTypes: ['dropdown'] },
		{
			label: '差分の平均',
			key: 'avgDiffOriginalNeckAngle',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{
			label: '差分の平均の平均',
			key: 'avgDiffAvgNeckAngle',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{
			label: '差分の平均の標準偏差',
			key: 'stdDiffAvgNeckAngle',
			type: 'number',
			availableUiTypes: ['dropdown']
		}
	];

	const api = createAnnotationApi({
		basePath: import.meta.env.VITE_API_CLIENT_URL,
		token: data?.user?.token || ''
	});
	const loadMore = async ({
		page,
		size,
		refresh
	}: {
		page: number;
		size: number;
		refresh: boolean;
	}) => {
		if (!data.data) {
			const res = await api.getAnnotationSummaryByAnnotater({ page: 0, size });
			data = { ...data, data: res };
			return;
		}
		if (
			page >= data.data.totalPages ||
			data.data.isLast ||
			(page + 1) * size <= data.data.contents.length
		) {
			return;
		}

		try {
			const res = await api.getAnnotationSummaryByAnnotater({ page, size });
			data.data = refresh
				? res
				: {
						...data.data,
						...res,
						contents: [...data.data.contents, ...res.contents]
					};
		} catch (e) {
			console.error(e);
		}
	};

	const mutateFilteredData = (data: AnnotationSummaryByAnnotater[]) => {
		filteredData = data;
	};

	let contents = [...(data.data?.contents ?? [])];

	let filteredData: AnnotationSummaryByAnnotater[] = [...contents];

	let displayData: AnnotationSummaryByAnnotater[] = [...filteredData];

	$: counts = {
		display: filteredData.length,
		total: data.data?.contents?.length ?? 0
	};

	$: {
		if (data.data?.contents.length !== contents.length) {
			contents = [...(data?.data?.contents || [])];
			filteredData = [...contents];
			displayData = [...filteredData];
		}
	}
</script>

{#if !data.data}
	<p>データがありません</p>
{:else}
	<div class="wrapper">
		<div class="container">
			<DataSortFilter
				{optionTemplate}
				bind:data={contents}
				bind:counts
				on:updateData={({ detail }) => mutateFilteredData(detail)}
				id="DataSortFilter_/admin/user"
			/>
		</div>
		<AnnotationSummaryTable {headers} data={displayData} {navigateToDetail} />
		<div>
			<InfinitePagenation
				bind:contents={filteredData}
				bind:displayData
				bind:isLast={data.data.isLast}
				on:loadMore={({ detail }) => loadMore(detail)}
				id="InfinitePagenation_/admin/user"
			/>
		</div>
	</div>
{/if}

<style lang="scss" scoped>
	@import '$lib/styles/_breakpoint';
	@import '$lib/styles/_mixins';
	.wrapper {
		text-align: center;
		width: 90%;
		max-width: 1600px;
		margin: 0 auto;
		padding: 12px 4px;

		@include mediaQuery('md') {
			padding: 16px 4px;
		}

		@include mediaQuery('lg') {
			padding: 32px 8px;
		}

		.container {
			margin-bottom: 16px;
		}
	}
</style>
