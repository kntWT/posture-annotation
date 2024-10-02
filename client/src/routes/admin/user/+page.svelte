<script lang="ts">
	import AnnotationSummaryTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummaryByAnnotater } from '$api/generated';
	import { goto } from '$app/navigation';
	import type { PageData } from './$types';
	import type { Header } from '$lib/components/admin/types/AnnotationSummaryTable';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	export let data: PageData;

	type Key = keyof AnnotationSummaryByAnnotater;

	const navigateToDetail = (data: AnnotationSummaryByAnnotater) => {
		console.log(data.annotaterId);
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

	let filteredData: AnnotationSummaryByAnnotater[] = [...(data.summary ?? [])];

	$: counts = {
		display: filteredData.length,
		total: data.summary?.length ?? 0
	};
</script>

{#if !data.summary}
	<p>データがありません</p>
{:else}
	<div class="wrapper">
		<div class="container">
			<DataSortFilter
				{optionTemplate}
				bind:data={data.summary}
				bind:counts
				on:updateData={(e) => (filteredData = e.detail)}
			/>
		</div>
		<AnnotationSummaryTable {headers} data={filteredData} {navigateToDetail} />
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
