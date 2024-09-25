<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';

	export let data: PageData;

	const handleClick = (id: number) => {
		const annotaterId = parseInt($page.url.searchParams.get('annotater_id') || '');
		sessionStorage.setItem(
			`${import.meta.env.VITE_COOKIE_PREFIX}next_path`,
			JSON.stringify({
				path: '/admin/user/detail',
				param: annotaterId
			})
		);
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
