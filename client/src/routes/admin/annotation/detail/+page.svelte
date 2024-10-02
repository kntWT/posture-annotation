<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';

	export let data: PageData;

	const handleOnClick = (id: number) => {
		const postureId = parseInt($page.url.searchParams.get('posture_id') || '');
		sessionStorage.setItem(
			`${import.meta.env.VITE_COOKIE_PREFIX}next_path`,
			JSON.stringify({
				path: '/admin/annotation/detail',
				param: postureId
			})
		);
		goto(`${import.meta.env.VITE_BASE_PATH}/admin/edit?annotation_id=${id}`, {
			invalidateAll: true
		});
	};
</script>

{#if !data.data || data.data.annotations.length === 0}
	<p>データがありません</p>
{:else}
	<LayoutGrid>
		{#each data.data.annotations as annotation}
			<Cell
				class="card mdc-elevation-transition"
				spanDevices={{ desktop: 3, tablet: 6, phone: 12 }}
			>
				<AnnotationDetail
					{annotation}
					posture={data.data.posture}
					onClick={() => handleOnClick(annotation.id)}
				/>
			</Cell>
		{/each}
	</LayoutGrid>
{/if}
