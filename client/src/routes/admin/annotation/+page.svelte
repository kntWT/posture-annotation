<script lang="ts">
	import AnnotationSummaryTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummary } from '$api/generated';
	import { goto } from '$app/navigation';
	import type { PageData } from './$types';
	import type { Header } from '$lib/components/admin/types/AnnotationSummaryTable';
	export let data: PageData;
	type Data = Omit<AnnotationSummary, 'annotaterIds'> & {
		diffNeckAngle: number;
		count: number;
		annotaterIds: string;
	};
	const navigateToDetail = (data: Data) => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/annotation/detail?posture_id=${data.postureId}`, {
			invalidateAll: true
		});
	};
	const headers: Header<keyof Data>[] = [
		{
			display: 'id',
			key: 'postureId',
			isNumeric: false
		},
		{
			display: 'アノテーション数',
			key: 'count',
			isNumeric: true
		},
		{
			display: 'もとの首の角度',
			key: 'originalNeckAngle',
			isNumeric: true,
			digit: 4
		},
		{
			display: '角度の平均',
			key: 'avgNeckAngle',
			isNumeric: true,
			digit: 4
		},
		{
			display: '角度の標準偏差',
			key: 'stdNeckAngle',
			isNumeric: true,
			digit: 4,
			highlightThreshold: 10
		},
		{
			display: '角度の差',
			key: 'diffNeckAngle',
			isNumeric: true,
			digit: 4,
			highlightThreshold: 20
		},
		{
			display: 'アノテータ',
			key: 'annotaterIds',
			isNumeric: false
		}
	];

	$: summary =
		data.summary?.map((d) => {
			return {
				...d,
				diffNeckAngle: d.avgNeckAngle - d.originalNeckAngle,
				count: d.annotationIds.length,
				annotaterIds: d.annotaterIds.toSorted().join(', ')
			};
		}) ?? [];
</script>

{#if !data.summary}
	<p>データがありません</p>
{:else}
	<div class="wrapper">
		<AnnotationSummaryTable {headers} data={summary} {navigateToDetail} />
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
	}
</style>
