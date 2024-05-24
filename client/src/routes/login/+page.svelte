<script lang="ts">
	import Form from "./Form.svelte";
    import Divider from "$lib/components/Divider.svelte";
	import type { ActionData } from "./$types";
	import { goto } from "$app/navigation";
	import { onMount } from "svelte";

    export let form: ActionData
    onMount(() => {
        if (form?.status === 200) {
            goto("/");
        }
    });
</script>

<div class="wrapper">
    {#if form?.message}
        <div class="error-notification">
            <p>{form.action}に失敗しました</p>
            <p>{form.message}</p>
        </div>
    {/if}
    <Form title="ログイン" action="login" />
    <Divider color={"gray"} />
    <Form title="新規登録" action="signup" />
</div>

<style lang="scss">
    @import "$lib/styles/variables";
    .wrapper {
        display: flex;
        justify-content: center;
        flex-direction: column;
    }

    .error-notification {
        color: $error-color;
        padding: 10px;
        border-radius: 10%;
        border-color: $accent-color;
        margin: 10px;
    }
</style>