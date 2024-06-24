<script lang="ts">
	import { createPostureApi } from "$api";
	import type { PostureUpdateWithFile } from "$api/generated";
	import PostureAnnotater from "$lib/components/PostureAnnotater.svelte";
    import { getToken } from "$lib/store/user";
	import { onMount } from "svelte";
    import type { PageData } from "./$types";
	import { imageUrl } from "$lib/util";
	import { goto } from "$app/navigation";


    export let data: PageData;

    onMount(async () => {
        console.log(data)
    });

    const sendAnnotation = async (annotated: PostureUpdateWithFile) => {
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
            await postureApi.updatePostureById({ id: data.posture.id, postureUpdateWithFile: annotated });
            const path = window.location.pathname;
            goto(path, { invalidateAll: true });
            data = { ...data, posture: null };
        } catch (e) {
            console.error(e);
            alert("データの送信に失敗しました");
        }
    }

</script>

<div class="wrapper">
    <h1>姿勢アノテーション</h1>
    {#if data.posture }
        <PostureAnnotater
            posture={data.posture}
            imageSrc={imageUrl(data.posture, "original")}
            handleAction={sendAnnotation}
        />
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