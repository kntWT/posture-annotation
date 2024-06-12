<script lang="ts">
	import { createPostureApi } from "$api";
	import type { PostureUpdate } from "$api/generated";
	import PostureAnnotater from "$lib/components/PostureAnnotater.svelte";
    import { getToken } from "$lib/store/user";
	import { onMount } from "svelte";
    import type { PageData } from "./$types";


    export let data: PageData;

    onMount(async () => {
        console.log(data)
    });

    const sendAnnotation = async (annotated: PostureUpdate) => {
        if (!data.posture?.id) {
            return null;
        }
        const token = getToken();
        if (!token || token === "") {
            alert("ログインしてください");
            return;
        }
        const postureApi = createPostureApi({ token: token, basePath: import.meta.env.VITE_API_ENDPOINT });
        try {
            await postureApi.updatePostureById({ id: data.posture.id, postureUpdate: annotated });
        } catch (e) {
            console.error(e);
            alert("データの送信に失敗しました");
        }
    }

    const fromatDate = (date: Date | undefined) => {
        if (!date) {
            return "";
        }
        const year = date.getFullYear();
        const month = date.getMonth() + 1;
        const day = date.getDate();
        const hour = date.getHours();
        const minute = date.getMinutes();
        const second = date.getSeconds();
        const ms = date.getMilliseconds();
        const pad = (num: number, digit: number) => num.toString().padStart(digit, "0");
        return `${year}-${pad(month, 2)}-${pad(day, 2)}_${pad(hour, 2)}:${pad(minute, 2)}:${pad(second, 2)}.${pad(ms, 3)}`;
    }

    $: imageSrc = `/api/images/original/${data?.posture?.userId}/${fromatDate(data?.posture?.exCreatedAt)}.jpg`
</script>

<div class="wrapper">
    <h1>姿勢アノテーション</h1>
    {#if data.posture }
        <PostureAnnotater posture={data.posture} imageSrc={imageSrc} handleAction={sendAnnotation} />
    {:else}
        <p>データがありません</p>
    {/if}
</div>

<style lang="scss">
    .wrapper {
        text-align: center;
        height: fit-content;
        margin-bottom: 12px;

        h1 {
            text-align: center;
        }
    }
</style>