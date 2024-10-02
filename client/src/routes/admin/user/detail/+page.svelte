<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import type { AnnotationWithPosture } from '$api/generated';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';

	export let data: PageData;
	const POSTURE_PREFIX = 'posture_';
	type PosturePrefix = typeof POSTURE_PREFIX;
	type Content = AnnotationWithPosture['annotation'] & {
		[K in keyof AnnotationWithPosture['posture'] as `${PosturePrefix}${string & K}`]: AnnotationWithPosture['posture'][K];
	} & { _diffNeckAngle: number };
	type Key = keyof Content;
	type Count = {
		display: number;
		total: number;
	};

	const formatData = (d: AnnotationWithPosture): Content => {
		return {
			...d.annotation,
			...Object.fromEntries(
				Object.entries(d.posture).map(([key, value]) => [`${POSTURE_PREFIX}${key}`, value])
			),
			_diffNeckAngle: d.annotation.neckAngle - d.posture.neckAngle
		};
	};

	const revertFormatData = (d: Content): AnnotationWithPosture => {
		return {
			annotation: Object.fromEntries(
				Object.entries(d).filter(([key]) => !key.startsWith(POSTURE_PREFIX) && !key.startsWith('_'))
			) as AnnotationWithPosture['annotation'],
			posture: Object.fromEntries(
				Object.entries(d)
					.filter(([key]) => key.startsWith(POSTURE_PREFIX))
					.map(([key, value]) => [key.slice(POSTURE_PREFIX.length), value])
			) as AnnotationWithPosture['posture']
		};
	};

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

	const optionTemplate: Option<Key>[] = [
		{ key: 'id', label: 'ID', type: 'number', availableUiTypes: ['dropdown'] },
		{ key: 'neckAngle', label: '首の角度', type: 'number', availableUiTypes: ['dropdown'] },
		{
			key: `${POSTURE_PREFIX}neckAngle`,
			label: 'もとの首の角度',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{
			key: '_diffNeckAngle',
			label: '首の角度の差',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{
			key: `${POSTURE_PREFIX}userId`,
			label: 'ユーザーID',
			type: 'number',
			availableUiTypes: ['dropdown']
		},
		{ key: 'createdAt', label: '作成日時', type: 'date', availableUiTypes: ['dropdown'] }
	];

	let formattedData: Content[] = [...(data.data?.map(formatData) || [])];

	let filteredData: Content[] = [...formattedData];

	$: counts = {
		display: filteredData.length,
		total: formattedData.length
	} as Count;
</script>

<div class="wrapper">
	{#if !data.data || data.data.length === 0}
		<p>データがありません</p>
	{:else}
		<div class="container">
			<DataSortFilter
				bind:data={formattedData}
				{optionTemplate}
				bind:counts
				on:updateData={(e) => (filteredData = e.detail)}
			/>
		</div>
		<LayoutGrid>
			{#each filteredData.map(revertFormatData) as { annotation, posture }}
				<Cell
					class="card mdc-elevation-transition"
					spanDevices={{ desktop: 4, tablet: 6, phone: 12 }}
				>
					<AnnotationDetail {annotation} {posture} onClick={() => handleClick(annotation.id)} />
				</Cell>
			{/each}
		</LayoutGrid>
	{/if}
</div>

<style lang="scss" scoped>
	.wrapper {
		padding: 16px;

		.container {
			margin-bottom: 16px;
			text-align: center;
		}
	}
</style>
