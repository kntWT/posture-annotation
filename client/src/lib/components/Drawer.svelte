<script lang="ts">
	import { createAnnotationApi } from "$api";
	import { goto } from "$app/navigation";
	import { page } from "$app/stores";
	import Divider from "$lib/components/Divider.svelte";
	import { user } from "$lib/store/user";
    import Drawer, { Header, Title, Subtitle, Content, Scrim } from "@smui/drawer";
    import List, { Item, Text } from "@smui/list";
	import { onMount } from "svelte";

    export let open: boolean = false;
    let annotationCount: number = 0;

    const menues = [
        { name: "ホーム", path: "/"},
        { name: "ログイン", path: "/login"},
        { name: "アノテーション", path: "/annotate"},
        { name: "アノテーション履歴", path: "/logs"},
    ]

    onMount(() => {
        if (!$user) return;

        const annotationApi = createAnnotationApi({ token: $user.token, basePath: "http://localhost:5172" });
        annotationApi.getAnnotationCountByAnnotaterId({ annotaterId: $user.id }).then((res) => {
            annotationCount = res;
        });
    });

    const navigateTo = (path: string) => {
        open = false;
        goto(path, {invalidateAll: true});
    }

    $: isCurrentPage = (path: string) =>$page.url.pathname === path;
</script>

<div class="drawer-container">
    <Drawer variant="modal" bind:open fixed={true}>
        <Header>
            <Title>姿勢アノテーションシステム</Title>
            <Subtitle>まず最初にログインしてください</Subtitle>
        </Header>
        <Content>
            <List>
                {#each menues as { name, path }}
                    <Item on:click={() => navigateTo(path)} activated={isCurrentPage(path)}>
                        <Text>{name}</Text>
                    </Item>
                {/each}
            </List>
            <Divider color="gray" width="95%" />
            <List>
                <Item>
                    <Text>アノテーション数: {annotationCount}件</Text>
                </Item>
            </List>
        </Content>
    </Drawer>
    <Scrim />
</div>

<style lang="scss">
    .drawer-container {
        :global(.mdc-drawer) {
            width: 30vw;
            min-width: 200px;
            max-width: 500px;
        }
    }
</style>
