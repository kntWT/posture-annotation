<script lang="ts">
	import AnnotationSummaryByPostureTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummaryByPosture } from '$api/generated';
	import { goto } from '$app/navigation';
	import type { PageData } from './$types';
	import type { Header } from '$lib/components/admin/types/AnnotationSummaryTable';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	export let data: PageData;
	type Data = Omit<AnnotationSummaryByPosture, 'annotaterIds'> & {
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
		{ label: '件数', key: 'count', type: 'number' },
		{ label: 'もとの首の角度', key: 'originalNeckAngle', type: 'number' },
		{ label: '角度の平均', key: 'avgNeckAngle', type: 'number' },
		{ label: '角度の標準偏差', key: 'stdNeckAngle', type: 'number' },
		{ label: '角度の差', key: 'diffNeckAngle', type: 'number' }
	];

	let formatData: Data[] = [
		...(data?.summary?.map((d) => ({
			...d,
			diffNeckAngle: d.avgNeckAngle - d.originalNeckAngle,
			count: d.annotationIds.length,
			annotaterIds: d.annotaterIds.join(', ')
		})) || [])
	];

	let filteredData: Data[] = [...formatData];

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
				bind:data={formatData}
				bind:counts
				on:updateData={(e) => (filteredData = e.detail)}
			/>
		</div>
		<AnnotationSummaryByPostureTable {headers} bind:data={filteredData} {navigateToDetail} />
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
