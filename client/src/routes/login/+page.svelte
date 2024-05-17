<script lang="ts">
	import type { UserCreate } from "$api/generated";
	import { goto } from "$app/navigation";
	import { userApi } from "$api/userApi";
	import { login } from "$lib/store/user";
	import Form from "./Form.svelte";
    import Divider from "$lib/components/Divider.svelte";

    const handleLogin = async (userInfo: UserCreate) => {
        await (userApi.loginUser(userInfo))
            .then((res) => {
                const user = res.data;
                goto("/");
                login(user);
            })
            .catch((err) => {
                console.log(err);
                alert("ログインに失敗しました");
                return null;
            });
    };

    const handleSignup = async (userInfo: UserCreate) => {
        await (userApi.createUser(userInfo))
            .then((res) => {
                const data = res.data;
                login(data);
            })
            .catch((err) => {
                console.log(err);
                alert("新規登録に失敗しました");
            });
    };
</script>

<div class="wrapper">
    <Form action="ログイン" handleSubmit={handleLogin} />
    <Divider color={"gray"} />
    <Form action="新規登録" handleSubmit={handleSignup} />
</div>

<style lang="scss">
    .wrapper {
        display: flex;
        justify-content: center;
        flex-direction: column;
    }
</style>