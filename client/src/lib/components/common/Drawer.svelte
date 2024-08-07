<script lang="ts">
	import { createAnnotationApi } from "$api";
	import { goto } from "$app/navigation";
	import { page } from "$app/stores";
	import Divider from "$lib/components/common/Divider.svelte";
	import { user } from "$lib/store/user";
    import Drawer, { Header, Title, Subtitle, Content, Scrim } from "@smui/drawer";
    import List, { Item, Text } from "@smui/list";
	import { onMount } from "svelte";

    export let open: boolean = false;
    let annotationCount = {
        sample: 0,
        prod: 0,
    };

    onMount(() => {
        if (!$user) return;

        const annotationApi = createAnnotationApi({ token: $user.token, basePath: "http://localhost:5172" });
        annotationApi.getProdAnnotationCountByAnnotaterId({ annotaterId: $user.id }).then((res) => {
            annotationCount.prod = res;
        });
        annotationApi.getSampleAnnotationCountByAnnotaterId({ annotaterId: $user.id }).then((res) => {
            annotationCount.sample = res;
        });
    });

    const navigateTo = (path: string) => {
        open = false;
        goto(path, {invalidateAll: true});
    }

    $: isCurrentPage = (path: string) =>$page.url.pathname === path;

    $: menues = [
        { name: "ホーム", path: "/"},
        $user ? { name: "ログアウト", path: "/logout" } : { name: "ログイン", path: "/login"},
        { name: "アノテーション練習", path: "/annotate/sample"},
        { name: "アノテーション", path: "/annotate"},
        { name: "アノテーション履歴", path: "/logs"},
    ]
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
                <Item ripple={false}>
                    <Text>アノテーション数: {annotationCount.sample + annotationCount.prod}件</Text>
                </Item>
                <Item ripple={false}>
                    <Text>練習用: {annotationCount.sample}件</Text>
                </Item>
                <Item ripple={false}>
                    <Text>本番用: {annotationCount.prod}件</Text>
                </Item>
            </List>
        </Content>
    </Drawer>
    <Scrim />
</div>

<style lang="scss" scoped>
    .drawer-container {
        :global(.mdc-drawer) {
            width: 30vw;
            min-width: 200px;
            max-width: 500px;
        }
    }
</style>
