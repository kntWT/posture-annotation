<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';
	import { goto } from '$app/navigation';

	export let data: PageData;

	const handleClick = (id: number) => {
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/edit?annotation_id=${id}`, {
			invalidateAll: true
		});
	};
</script>

{#if !data.data || data.data.length === 0}
	<p>データがありません</p>
{:else}
	<LayoutGrid>
		{#each data.data as { annotation, posture }}
			<Cell
				class="card mdc-elevation-transition"
				spanDevices={{ desktop: 4, tablet: 6, phone: 12 }}
			>
				<AnnotationDetail {annotation} {posture} onClick={() => handleClick(annotation.id)} />
			</Cell>
		{/each}
	</LayoutGrid>
{/if}
