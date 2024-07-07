<script lang="ts">
	import AnnotatedCard from "$lib/components/AnnotatedCard.svelte";
	import { onMount } from "svelte";
	import type { PageData } from "../$types";
	import { imageUrl } from "$lib/util";

    export let data: PageData;

    onMount(async () => {
        console.log(data)
    });
</script>

<div class="wrapper">
    <h1>アノテーション履歴</h1>
    {#if data.annotations.length === 0}
        <p>データがありません</p>
        <a href="/annotate">アノテーションをする</a>
    {:else}
        <div class="card-list">
            {#each data.annotations as annotation}
                <div class="card">
                    <AnnotatedCard annotation={annotation} imageSrc={imageUrl(annotation.userId, annotation.fileName, "annotated")} />
                </div>
            {/each}
        </div>
    {/if}
</div>

<style lang="scss">
    .wrapper {
        text-align: center;
        height: fit-content;
        margin-bottom: 12px;

        h1 {
            text-align: center;
        }

        .card-list {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
            gap: 12px;
            justify-content: center;

            .card {
                margin: auto;
                width: 80%;
            }
        }
    }
</style>