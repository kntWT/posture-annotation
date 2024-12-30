<script lang="ts">
	import type { Annotation, Posture } from '$api/generated';
	import { goto } from '$app/navigation';
	import { getHighlightColor } from '$lib/util';
	import Card, { Media, Content, PrimaryAction } from '@smui/card';

	export let annotation: Annotation;
	export let originalNeckAngle: number | null = null;
	export let imageSrc: string;
	export let showWaist: boolean = false;
	export let navigatePath: string = `${import.meta.env.VITE_BASE_PATH}/annotate`;
	const THRES = 20;

	const navigateTo = (id: number) => {
		goto(`${navigatePath}?id=${id}`, { invalidateAll: true });
	};

	$: diffNeckAngle = originalNeckAngle ? annotation.neckAngle - originalNeckAngle : 0;
	$: userId = imageSrc.match(/\/(\d+|sample)/)?.[1] || 0;
</script>

<Card>
	<PrimaryAction on:click={() => navigateTo(annotation.postureId)}>
		<Content style="aspect-ratio: 4/1">
			<span>#{annotation.postureId} ({annotation.updatedAt?.toLocaleDateString?.()})</span><br />
			<span>
				首の角度: {annotation.neckAngle.toFixed(2)}
				{#if originalNeckAngle !== null}
					(<span class={getHighlightColor(diffNeckAngle, THRES)}>
						{diffNeckAngle < 0 ? '' : '+'}{diffNeckAngle.toFixed(2)}
					</span>)
				{/if}
			</span>
			<br />
			{#if showWaist}
				<span>胴体の角度: {annotation.torsoAngle.toFixed(2)}</span><br />
			{/if}
			<span>ユーザーID: {userId}</span><br />
		</Content>
		<Media style={`background-image: url(${imageSrc})`} aspectRatio="16x9" />
	</PrimaryAction>
</Card>

<style lang="scss" scoped>
	@import '$lib/styles/_variables';
	span {
		padding: 4px;
		line-height: 1.2rem;
	}
	.red {
		background-color: $highlight-red;
	}

	.green {
		background-color: $highlight-green;
	}
</style>
