<script lang="ts">
	import type { Annotation } from "$api/generated";

    export let annotation: Annotation;
    export let imageSrc: string;
    export let showWaist: boolean = false;

</script>

<div class="wrapper">
    <div>
        <span><a href={`/annotate?id=${annotation.postureId}`}>#{annotation.postureId}</a>({annotation.updatedAt?.toLocaleDateString?.()})</span><br />
        <span>首の角度: {annotation.neckAngle.toFixed(2)}</span><br />
        {#if showWaist}
            <span>胴体の角度: {annotation.torsoAngle.toFixed(2)}</span><br />
        {/if}
    </div>
    <img src={imageSrc} alt="annotated-posture" />
</div>

<style lang="scss">
    .wrapper {
        display: flex;
        flex-direction: column;
        align-items: center;
        border: 1px solid #ccc;
        padding: 12px;

        div {
            display: block;
            margin: 8px;
        }
    }
    img {
        width: 80%;
        max-width: 500px;
    }
</style>