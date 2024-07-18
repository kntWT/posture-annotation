<script lang="ts">
    import { goto } from "$app/navigation";
    import { user } from "$lib/store/user";
    import { page } from "$app/stores";
    import TopAppBar, { Section, Title, Row } from "@smui/top-app-bar";
    import IconButton from "@smui/icon-button";
    import Button, { Label } from "@smui/button";
    import Ripple from "@smui/ripple";
    import Drawer from "$lib/components/Drawer.svelte";

    let topAppBar: TopAppBar;
    let openDrawer: boolean = false;

    const navigateTo = (path: string) => {
        goto(path);
    }

    const handleSwitchDrawer = () => {
        openDrawer = !openDrawer;
    }
</script>

<TopAppBar bind:this={topAppBar} variant="standard">
    <Row>
        <Section align="start">
            <IconButton on:click={handleSwitchDrawer} class="material-icons">menu</IconButton>
        </Section>
        <Section>
            <Title
                class="center"
                style="width: 100%; text-align: center;"
                on:click={() => navigateTo("/")}
            >
                <div
                    use:Ripple={{ unbounded: false }}
                    style="cursor: pointer; width: 100%; text-align: center;"
                    role="button"
                >
                    姿勢アノテーションシステム
                </div>
            </Title>
        </Section>
        <Section align="end" toolbar>
            {#if $page.url.pathname !== "/login"}
                {#if $user}
                    <Button on:click={() => navigateTo("/logout")} variant="raised">
                        <Label>
                            ログアウト
                        </Label>
                    </Button>
                {:else }
                    <Button on:click={() => navigateTo("/login")} variant="raised">
                        <Label>
                            ログイン
                        </Label>
                    </Button>
                {/if}
            {/if}
        </Section>
    </Row>
    {#if openDrawer}
        <Drawer bind:open={openDrawer} />
    {/if}
</TopAppBar>

<style lang="scss" scoped>
    :global(.mdc-top-app-bar) {
        position: relative !important;
    }
</style>