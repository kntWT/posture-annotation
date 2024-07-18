<script lang="ts">
	import AnnotatedCard from "$lib/components/AnnotatedCard.svelte";
	import { onMount } from "svelte";
	import type { PageData } from "../$types";
    import LayoutGrid, { Cell } from "@smui/layout-grid";
    import Accordion, { Panel, Header, Content } from "@smui-extra/accordion";
    import IconButton, { Icon } from "@smui/icon-button";
	import { imageUrl } from "$lib/util";

    export let data: PageData;

    const contents = [
        {
            title: "サンプルデータ",
            data: data.samples,
            open: data.samples.length === 0,
            subPath: "sample",
        },
        {
            title: "アノテーションデータ",
            data: data.annotations,
            open: data.annotations.length === 0,
        }
    ]

    onMount(async () => {
        console.log(data)
    });
</script>

<div class="wrapper">
    <h1>アノテーション履歴</h1>
    <Accordion multiple>
        {#each contents as content}
            <Panel
                bind:open={content.open}
            >
                <Header>
                    {content.title}（{content.data.length}件）
                    <IconButton slot="icon" toggle pressed={content.open}>
                        <Icon class="material-icons" on>unfold_less</Icon>
                        <Icon class="material-icons">unfold_more</Icon>
                    </IconButton>
                </Header>
                <Content>
                    {#if content.data.length === 0}
                        <Content>
                            <p>データがありません</p>
                        </Content>
                    {:else}
                        <LayoutGrid>
                            {#each content.data as annotation}
                                <Cell class="card mdc-elevation-transition" spanDevices={{ desktop: 2, tablet: 2, phone: 4 }}>
                                    <AnnotatedCard
                                        annotation={annotation}
                                        navigatePath={`/annotate${content.subPath ? `/${content.subPath}` : ""}`}
                                        imageSrc={imageUrl(
                                            {
                                                userId: content.subPath ?? annotation.userId,
                                                annotaterId: data.user?.id,
                                                fileName: annotation.fileName
                                            },
                                            "annotated"
                                        )}
                                    />
                                </Cell>
                            {/each}
                        </LayoutGrid>
                    {/if}
                </Content>
            </Panel>
        {/each}
    </Accordion>
</div>

<style lang="scss" scoped>
    @import "$lib/styles/variables.scss";

    .wrapper {
        text-align: center;
        height: fit-content;
        margin-bottom: 12px;

        :global(.smui-accordion__panel) {
            margin-bottom: 24px;

            :global(.smui-accordion__header) {
                background-color: $base-color;
            }

            :global(.smui-paper__content) {
                // background-color: $secondary-color;
                background-color: grayscale($color: $base-color);
            }
        }

        h1 {
            text-align: center;
        }

        .card {
            margin: auto;
            width: 80%;
        }
    }
</style>