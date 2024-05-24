<script lang="ts">
    export let title: string;
    export let action: string;

    let name = "";
    let password = "";

    $: isSubmittable = name.length > 0 && password.length > 0;
</script>

<div class="form">
    <h1>{title}</h1>
    <form method="post" action={`?/${action}`}>
        <div class="form-group">
            <label for={`${action}-name`}>ユーザ名</label>
            <input type="text" name="name" id={`${action}-name`} bind:value={name} required>
        </div>
        <div class="form-group">
            <label for={`${action}-password`}>パスワード</label>
            <input type="password" name="password" id={`${action}-password`} bind:value={password} required>
        </div>
        <button disabled={!isSubmittable}>ログイン</button>
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