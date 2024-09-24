<script lang="ts">
	import AnnotationSummaryTable from '$lib/components/admin/AnnotationSummaryTable.svelte';
	import type { AnnotationSummary } from '$api/generated';
	import { goto } from '$app/navigation';

	import type { PageData } from './$types';

	export let data: PageData;

	type Data = AnnotationSummary & {
		diffNeckAngle: number;
		count: number;
	};
	const navigateToDetail = (id: number) => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/annotation/detail?posture_id=${id}`, {
			invalidateAll: true
		});
	};
</script>

{#if !data.summary}
	<p>データがありません</p>
{:else}
	<div class="wrapper">
		<AnnotationSummaryTable data={data.summary} {navigateToDetail} />
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
