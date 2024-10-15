<script lang="ts">
	import AnnotationSummaryTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummaryByPostureWithPageInfo } from '$api/generated';
	import { goto } from '$app/navigation';
	import type { PageData } from './$types';
	import type { Header } from '$lib/components/admin/types/AnnotationSummaryTable';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import InfinitePagenation from '$lib/components/common/InfinitePagenation.svelte';
	import { createAnnotationApi } from '$api';
	export let data: PageData;
	type Data = Omit<AnnotationSummaryByPostureWithPageInfo['contents'][number], 'annotaterIds'> & {
		diffNeckAngle: number;
		count: number;
		annotaterIds: string;
	};
	type Key = keyof Data;
	const navigateToDetail = (data: Data) => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/annotation/detail?posture_id=${data.postureId}`, {
			invalidateAll: true
		});
	};
	const headers: Header<Key>[] = [
		{
			display: 'id',
			key: 'postureId',
			type: 'string'
		},
		{
			display: '画像',
			key: 'fileName',
			type: 'image',
			clickable: false
		},
		{
			display: '件数',
			key: 'count',
			type: 'number'
		},
		{
			display: 'もとの首の角度',
			key: 'originalNeckAngle',
			type: 'number',
			digit: 4
		},
		{
			display: '角度の平均',
			key: 'avgNeckAngle',
			type: 'number',
			digit: 4
		},
		{
			display: '角度の標準偏差',
			key: 'stdNeckAngle',
			type: 'number',
			digit: 4,
			highlightThreshold: 10
		},
		{
			display: '角度の差',
			key: 'diffNeckAngle',
			type: 'number',
			digit: 4,
			highlightThreshold: 20
		},
		{
			display: 'アノテータ',
			key: 'annotaterIds',
			type: 'string'
		}
	];

	const optionTemplate: Option<Key>[] = [
		{ label: 'ID', key: 'postureId', type: 'number', availableUiTypes: ['dropdown'] },
		{ label: '件数', key: 'count', type: 'number', availableUiTypes: ['dropdown'] },
		{
			label: 'もとの首の角度',
			key: 'originalNeckAngle',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{ label: '角度の平均', key: 'avgNeckAngle', type: 'number', availableUiTypes: ['dropdown'] },
		{
			label: '角度の標準偏差',
			key: 'stdNeckAngle',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{ label: '角度の差', key: 'diffNeckAngle', type: 'number', availableUiTypes: ['dropdown'] },
		{
			label: '本番用',
			key: 'isSample',
			type: 'boolean',
			availableUiTypes: ['checkbox'],
			checkboxConfigs: [
				{
					label: '本番用',
					value: 'false'
				},
				{
					label: 'サンプル',
					value: 'true'
				}
			]
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
			const res = await api.getAnnotationSummaryByPosture({ page: 0, size });
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
			const res = await api.getAnnotationSummaryByPosture({ page, size });
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

	const mutateFilteredData = (data: Data[]) => {
		filteredData = data;
	};

	let formatData: Data[] = [
		...(data?.data?.contents?.map((d) => ({
			...d,
			diffNeckAngle: d.avgNeckAngle - d.originalNeckAngle,
			count: d.annotationIds.length,
			annotaterIds: d.annotaterIds.join(', ')
		})) || [])
	];

	let filteredData: Data[] = [...formatData];

	let displayData: Data[] = [...filteredData];

	$: counts = {
		display: filteredData.length,
		total: data.data?.contents?.length ?? 0
	};

	$: {
		if (data.data?.contents.length !== formatData.length) {
			formatData = [
				...(data?.data?.contents?.map((d) => ({
					...d,
					diffNeckAngle: d.avgNeckAngle - d.originalNeckAngle,
					count: d.annotationIds.length,
					annotaterIds: d.annotaterIds.join(', ')
				})) || [])
			];
			filteredData = [...formatData];
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
				bind:data={formatData}
				bind:counts
				on:updateData={(e) => mutateFilteredData(e.detail)}
				id="/admin/annotation"
			/>
		</div>
		<AnnotationSummaryTable {headers} bind:data={displayData} {navigateToDetail} />
		<div>
			<InfinitePagenation
				bind:contents={filteredData}
				bind:displayData
				bind:isLast={data.data.isLast}
				on:loadMore={(e) => loadMore(e.detail)}
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
		overflow-x: scroll;

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
