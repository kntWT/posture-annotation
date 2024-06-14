<script lang="ts">
	import type { Posture } from "$api/generated";

    export let posture: Posture;
    export let imageSrc: string;

</script>

<div class="wrapper">
    <div>
        <span><a href={`/annotate?id=${posture.id}`}>#{posture.id}</a>({posture.updatedAt.toLocaleDateString()})</span><br />
        <span>首の角度: {posture.neckAngle}</span><br />
        <span>胴体の角度: {posture.torsoAngle}</span><br />
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