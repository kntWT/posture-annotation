<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';

	export let data: PageData;
</script>

{#if !data.data || data.data.annotations.length === 0}
	<p>データがありません</p>
{:else}
	<LayoutGrid>
		{#each data.data.annotations as annotation}
			<Cell
				class="card mdc-elevation-transition"
				spanDevices={{ desktop: 4, tablet: 6, phone: 12 }}
			>
				<AnnotationDetail {annotation} posture={data.data.posture} />
			</Cell>
		{/each}
	</LayoutGrid>
{/if}
