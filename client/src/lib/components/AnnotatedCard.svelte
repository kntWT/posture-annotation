<script lang="ts">
	import type { Annotation } from '$api/generated';
	import { goto } from '$app/navigation';
	import Card, { Media, Content, PrimaryAction } from '@smui/card';

	export let annotation: Annotation;
	export let imageSrc: string;
	export let showWaist: boolean = false;
    export let navigatePath: string = "/annotate";

	const navigateTo = (id: number) => {
		goto(`${navigatePath}?id=${id}`, { invalidateAll: true });
	};
</script>

<Card>
    <PrimaryAction on:click={() => navigateTo(annotation.postureId)}>
        <Content style="aspect-ratio: 2/1">
            <span>#{annotation.postureId} ({annotation.updatedAt?.toLocaleDateString?.()})</span><br />
            <span>首の角度: {annotation.neckAngle.toFixed(2)}</span><br />
            {#if showWaist}
                <span>胴体の角度: {annotation.torsoAngle.toFixed(2)}</span><br />
            {/if}
        </Content>
        <Media
            style={`background-image: url(${imageSrc})`}
            aspectRatio="16x9"
        />
    </PrimaryAction>
</Card>

<style lang="scss" scoped>
</style>
