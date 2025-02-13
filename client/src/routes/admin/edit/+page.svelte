<script lang="ts">
	import { createAnnotationApi } from '$api';
	import type { AnnotationCreateWithFile } from '$api/generated';
	import { goto } from '$app/navigation';
	import PostureAnnotater from '$lib/components/annotate/PostureAnnotater.svelte';
	import { getToken } from '$lib/store/user';
	import { imageUrlFromPosture } from '$lib/util';
	import type { PageData } from './$types';

	type AvailablePaths =
		| '/admin'
		| '/admin/annotation'
		| '/admin/annotation/detail'
		| '/admin/user'
		| '/admin/user/detail';

	export let data: PageData;
	type NextPath = { path: AvailablePaths; param: number } | undefined;

	const onSuccess = () => {
		const nextPath = JSON.parse(
			sessionStorage.getItem(`${import.meta.env.VITE_COOKIE_PREFIX}next_path`) || 'undefined'
		) as NextPath;
		if (!nextPath) {
			location.replace(`${import.meta.env.VITE_BASE_PATH}/admin`);
			goto(`${import.meta.env.VITE_BASE_PATH}/admin`, { invalidateAll: true });
			return;
		}

		let param = '';
		if (nextPath.path === '/admin/annotation/detail') {
			param = `?posture_id=${nextPath.param}`;
		} else if (nextPath.path === '/admin/user/detail') {
			param = `?annotater_id=${nextPath.param}`;
		}
		const url = `${import.meta.env.VITE_BASE_PATH}${nextPath.path}${param}`;
		location.replace(url);
		history.go(-1);
		goto(url, { invalidateAll: true });
	};

	const onError = (e?: Error) => {
		console.error(e);
		// FIXME: なぜか再レンダリングしても破棄されたPostureAnnotaterのsubmitが呼ばれてしまうので，無理矢理リセット
		window.location.href = `${import.meta.env.VITE_BASE_PATH}/admin/edit?annotation_id=${data.data?.annotation.id}`;
		// alert("データの送信に失敗しました");
	};

	const sendAnnotation = async (annotated: AnnotationCreateWithFile) => {
		if (!data.data?.posture.id || !data.data.annotation?.annotaterId) {
			return null;
		}
		const token = getToken();
		if (!token || token === '') {
			alert('ログインしてください');
			return;
		}
		const annotationApi = createAnnotationApi({
			token: token,
			basePath: import.meta.env.VITE_API_CLIENT_URL
		});
		try {
			await annotationApi.createOrUpdateAnnotation({ annotationCreateWithFile: annotated });
			onSuccess();
		} catch (e) {
			onError(e as Error);
		}
	};
</script>

<div class="annotation-wrapper">
	{#if data.data?.posture && data.data.annotation}
		<PostureAnnotater
			posture={data.data.posture}
			annotaterId={data.data.annotation.annotaterId}
			imageSrc={imageUrlFromPosture(
				data.data.posture,
				data.data.annotation.annotaterId,
				'original'
			)}
			handleAction={sendAnnotation}
			showWaist={false}
			holdShoulder={true}
		/>
	{:else}
		<p>データがありません</p>
	{/if}
</div>

<style lang="scss" scoped>
	.annotation-wrapper {
		padding: 24px 0;
		text-align: center;
		height: fit-content;
		margin-bottom: 12px;
		overflow: hidden;

		position: fixed !important;
		-webkit-touch-callout: none;
		-webkit-user-select: none;
		user-select: none;
	}
</style>
