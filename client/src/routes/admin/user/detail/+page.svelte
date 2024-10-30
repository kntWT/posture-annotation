<script lang="ts">
	import AnnotationDetail from '$lib/components/admin/AnnotationDetail.svelte';
	import LayoutGrid, { Cell } from '@smui/layout-grid';
	import type { PageData } from './$types';
	import { goto } from '$app/navigation';
	import { page } from '$app/stores';
	import DataSortFilter from '$lib/components/dataIntercepter/DataSortFilter.svelte';
	import type { AnnotationWithPostureAndPageInfo } from '$api/generated';
	import type { Option } from '$lib/components/dataIntercepter/types/Option';
	import InfinitePagenation from '$lib/components/common/InfinitePagenation.svelte';
	import { createAnnotationApi } from '$api';
	import { mergeArray } from '$lib/util';

	export let data: PageData;
	const POSTURE_PREFIX = 'posture_';
	type PosturePrefix = typeof POSTURE_PREFIX;
	type Data = AnnotationWithPostureAndPageInfo['contents'][number];
	type Content = Data['annotation'] & {
		[K in keyof Data['posture'] as `${PosturePrefix}${string & K}`]: Data['posture'][K];
	} & { _diffNeckAngle: number };
	type Key = keyof Content;
	type Count = {
		display: number;
		total: number;
	};

	const annotaterId = parseInt($page.url.searchParams.get('annotater_id') || '');

	const formatData = (d: Data): Content => {
		return {
			...d.annotation,
			...Object.fromEntries(
				Object.entries(d.posture).map(([key, value]) => [`${POSTURE_PREFIX}${key}`, value])
			),
			_diffNeckAngle: d.annotation.neckAngle - d.posture.neckAngle
		};
	};

	const revertFormatData = (d: Content): Data => {
		return {
			annotation: Object.fromEntries(
				Object.entries(d).filter(([key]) => !key.startsWith(POSTURE_PREFIX) && !key.startsWith('_'))
			) as Data['annotation'],
			posture: Object.fromEntries(
				Object.entries(d)
					.filter(([key]) => key.startsWith(POSTURE_PREFIX))
					.map(([key, value]) => [key.slice(POSTURE_PREFIX.length), value])
			) as Data['posture']
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
		{ key: 'createdAt', label: '作成日時', type: 'date', availableUiTypes: ['dropdown'] },
		{
			label: '本番用',
			key: 'posture_isSample',
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
			const res = await api.getAnnotationsWithPostureByAnnotaterId({ annotaterId, page: 0, size });
			data = { ...data, data: res };
			return;
		}
		if (
			page > data.data.totalPages ||
			data.data.isLast ||
			(page + 1) * size <= displayData.length
		) {
			return;
		}

		const _page = refresh ? 0 : Math.ceil(data.data.contents.length / size);

		try {
			const res = await api.getAnnotationsWithPostureByAnnotaterId({
				annotaterId,
				page: _page,
				size
			});
			data.data = refresh
				? res
				: {
						...data.data,
						...res,
						contents: mergeArray(data.data.contents, res.contents, (c) => c.annotation.id)
					};
		} catch (e) {
			console.error(e);
		}
	};

	const mutateFilteredData = (data: Content[]) => {
		filteredData = data;
	};

	let formattedData: Content[] = [...(data.data?.contents?.map(formatData) || [])];

	let filteredData: Content[] = [...formattedData];

	let displayData: Content[] = [...filteredData];

	$: counts = {
		display: filteredData.length,
		total: formattedData.length
	} as Count;

	$: {
		if (data.data?.contents.length !== formattedData.length) {
			formattedData = [...(data?.data?.contents?.map(formatData) || [])];
			filteredData = [...formattedData];
			displayData = [...filteredData];
		}
	}
</script>

<div class="wrapper">
	{#if !data.data || data.data.contents.length === 0}
		<p>データがありません</p>
	{:else}
		<div class="container">
			<DataSortFilter
				bind:data={formattedData}
				{optionTemplate}
				bind:counts
				on:updateData={(e) => mutateFilteredData(e.detail)}
				id="DataSortFilter_/admin/user/detail"
			/>
		</div>
		<LayoutGrid>
			{#each displayData.map(revertFormatData) as { annotation, posture }}
				<Cell
					class="card mdc-elevation-transition"
					spanDevices={{ desktop: 3, tablet: 6, phone: 12 }}
				>
					<AnnotationDetail {annotation} {posture} onClick={() => handleClick(annotation.id)} />
				</Cell>
			{/each}
		</LayoutGrid>
		<div>
			<InfinitePagenation
				bind:contents={filteredData}
				bind:displayData
				bind:isLast={data.data.isLast}
				on:loadMore={({ detail }) => {
					loadMore(detail);
				}}
				id="InfinitePagenation_/admin/user/detail"
			/>
		</div>
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
