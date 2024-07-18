<script lang="ts">
	import Form from "./Form.svelte";
    import Divider from "$lib/components/Divider.svelte";
	import type { ActionData } from "./$types";
	import { goto, invalidateAll } from "$app/navigation";
	import { onMount } from "svelte";
	import type { UserCreate } from "$api/generated";

    export let form: ActionData

    let isMounted = false;
    onMount(() => {
        if (form?.status === 200) {
            goto("/");
        }
        isMounted = true;
    });

    const handleSubmit = (action: "login" | "signup") => {
        return async (values: UserCreate) => {
            const formData = new FormData();
            formData.append("name", values.name);
            formData.append("password", values.password);
            // FIXME: 全てのコンポーネントをリロードせずに再レンダリングしたい
            await fetch(`?/${action}`, {
                method: "POST",
                body: formData,
            })
            .then(res => res.json())
            .then((res) => {
                console.log(res)
                if (res.status === 200) {
                    window.location.reload();
                } else {
                    const data = JSON.parse(res.data);
                    const message = data[data[0].message];
                    alert(message);
                }
            })
            .catch((e) => {
                console.error(e);
                alert("エラーが発生しました")
            })
        }
    }
</script>

<div class="wrapper">
    {#if form?.message}
        <div class="error-notification">
            <p>{form.action}に失敗しました</p>
            <p>{form.message}</p>
        </div>
    {/if}
    <!-- NOTE: felteなどがSSR対応してないので，mount後にレンダリング -->
    {#if !isMounted}
        <p>読み込み中...</p>
    {:else}
        <Form title="ログイン" action="login" handleSubmit={handleSubmit("login")} />
        <Divider color={"gray"} width="90vw" />
        <Form title="新規登録" action="signup" handleSubmit={handleSubmit("signup")} />
    {/if}
</div>

<style lang="scss" scoped>
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