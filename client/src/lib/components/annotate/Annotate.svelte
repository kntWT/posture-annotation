<script lang="ts">
	import { createAnnotationApi } from '$api';
	import type {
		AnnotationCreateWithFile,
		AnnotationUpdateWithFile,
		Posture,
		User
	} from '$api/generated';
	import PostureAnnotater from '$lib/components/annotate/PostureAnnotater.svelte';
	import { getToken } from '$lib/store/user';
	import { imageUrlFromPosture } from '$lib/util';
	import {
		annotationHistory,
		undo,
		addHistory,
		type AnnotationHistory
	} from '$lib/store/annotationHistory';
	import Button from '@smui/button';

	export let posture: Posture | null = null;
	export let user: User | null = null;

	export let handlePostUndo: (dst: number) => void;
	export let onSuccess: () => void;
	export let onError: (e?: Error) => void;

	$: isOldest = $annotationHistory.currentIndex === -1;

	const handleUndo = () => {
		const dst = undo($annotationHistory);
		if (dst <= 0) return;

		handlePostUndo(dst);
	};

	const sendAnnotation = async (annotated: AnnotationCreateWithFile) => {
		if (!posture?.id || !user?.id) {
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
			addHistory(posture.id);
			onSuccess();
		} catch (e) {
			onError(e as Error);
		}
	};
</script>

<div class="wrapper">
	<Button on:click={handleUndo} disabled={isOldest} variant="raised">直前に戻る</Button>
	{#if posture}
		<PostureAnnotater
			{posture}
			imageSrc={imageUrlFromPosture(posture, user?.id ?? 1, 'original')}
			handleAction={sendAnnotation}
			showWaist={false}
			holdShoulder={true}
		/>
	{:else}
		<p>データがありません</p>
	{/if}
</div>

<style lang="scss" scoped>
	.wrapper {
		padding: 24px 0;
		text-align: center;
		height: fit-content;
		margin-bottom: 12px;
		overflow: hidden;

		h1 {
			text-align: center;
		}

		button {
			padding: 4px 8px;
		}
	}
</style>
