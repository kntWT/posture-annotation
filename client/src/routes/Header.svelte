<script lang="ts">
    import { goto } from "$app/navigation";
    import { user, logout } from "$lib/store/user";
    import { page } from "$app/stores";

    const navigateLogin = () => {
        goto("/login");
    };
    const handleLogout = async () => {
        logout();
        goto("/logout");
    };
</script>

<div class="header">
    <h2>姿勢アノテーションシステム</h2>
    {#if $page.url.pathname !== "/login"}
        {#if $user}
            <btn on:click={handleLogout}>ログアウト</btn>
        {:else }
            <btn on:click={navigateLogin}>ログイン</btn>
        {/if}
    {/if}
</div>

<style lang="scss">

    @import "$lib/styles/variables";

    .header {
        background-color: $base-color;
        text-align: center;
        width: 100%;
        height: 10vh;
        min-height: 80px;
        max-height: 150px;
        position: relative;
        align-items: center;

        h2 {
            margin: auto;
            position: absolute;
            max-width: 70%;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            -webkit-transform: translate(-50%, -50%);
        }

        btn {
            background-color: $accent-color;
            color: $secondary-color;
            padding: 8px 8px;
            margin: auto 16px auto auto;
            border: none;
            cursor: pointer;
            width: 15%;
            max-width: 200px;
            position: absolute;
            top: 50%;
            right: 0;
            transform: translate(0, -50%);
            -webkit-transform: translate(0, -50%);
            font-size: 0.7rem;
        }
    }

</style>