<script lang="ts">
    import { goto } from "$app/navigation";
    import { user, logout } from "$lib/store/user";
    import { page } from "$app/stores";
    import TopAppBar, { Section, Title, Row } from "@smui/top-app-bar";
    import IconButton from "@smui/icon-button";
    import Button, { Label } from "@smui/button";
    import Ripple from "@smui/ripple";

    let topAppBar: TopAppBar;

    const navigateHome = () => {
        goto("/");
    };

    const navigateLogin = () => {
        goto("/login");
    };
    const handleLogout = async () => {
        logout();
        goto("/logout");
    };
</script>

<TopAppBar bind:this={topAppBar} variant="standard">
    <Row>
        <Section align="start">
            <IconButton class="material-icons">menu</IconButton>
        </Section>
        <Section>
            <Title
                class="center"
                style="width: 100%; text-align: center;"
                on:click={navigateHome}
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
                    <Button on:click={handleLogout}>
                        <Label>
                            ログアウト
                        </Label>
                    </Button>
                {:else }
                    <Button on:click={navigateLogin}>
                        <Label>
                            ログイン
                        </Label>
                    </Button>
                {/if}
            {/if}
        </Section>
    </Row>
</TopAppBar>

<style lang="scss">
    :global(.mdc-top-app-bar) {
        position: relative !important;
    }
</style>