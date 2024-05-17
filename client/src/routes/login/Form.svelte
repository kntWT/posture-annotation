<script lang="ts">
	import type { UserCreate } from "$api/generated";

    export let action: string;
    export let handleSubmit: (userInfo: UserCreate) => void;

    let name = "";
    let password = "";

    $: isSubmittable = name.length > 0 && password.length > 0;

    const onSubmit = () => {
        if (!isSubmittable) {
            alert("ユーザ名とパスワードを入力してください");
            return;
        }
        handleSubmit({ name, password });
    };
</script>

<div class="form">
    <h1>{action}</h1>
    <form >
        <div class="form-group">
            <label for="name">ユーザ名</label>
            <input type="text" bind:value={name} required>
        </div>
        <div class="form-group">
            <label for="password">パスワード</label>
            <input type="password" bind:value={password} required>
        </div>
        <button on:click={onSubmit} disabled={!isSubmittable}>ログイン</button>
    </form>
</div>

<style lang="scss">
    @import "$lib/styles/variables";

    .form {
        margin: 20px;
    }

    .form-group {
        width: fit-content;
        margin: 10px auto;

        label {
            display: inline-block;
            width: 100px;
        }
    }

    h1 {
        text-align: center;
    }

    button {
        margin: 20px auto;
        display: block;
        width: 20%;
        height: 2rem;
        &:not(:disabled) {
            background-color: $base-color;
            border: none;
        }
    }
</style>