<script lang="ts">
	import AnnotatedCard from "$lib/components/AnnotatedCard.svelte";
	import { onMount } from "svelte";
	import type { PageData } from "../$types";
    import LayoutGrid, { Cell } from "@smui/layout-grid";
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
        <LayoutGrid>
            {#each data.annotations as annotation}
                <Cell class="card" spanDevices={{ desktop: 2, tablet: 2, phone: 4 }}>
                    <AnnotatedCard
                        annotation={annotation}
                        imageSrc={imageUrl(annotation.userId, annotation.fileName, "annotated")}
                    />
                </Cell>
            {/each}
        </LayoutGrid>
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

        .card {
            margin: auto;
            width: 80%;
        }
    }
</style>