<script lang="ts">
	import { createAnnotationApi } from "$api";
	import type { AnnotationCreateWithFile, AnnotationUpdateWithFile } from "$api/generated";
	import PostureAnnotater from "$lib/components/PostureAnnotater.svelte";
    import { getToken } from "$lib/store/user";
	import { onMount } from "svelte";
    import type { PageData } from "./$types";
	import { imageUrlFromPosture } from "$lib/util";
	import { goto } from "$app/navigation";
	import { page } from "$app/stores";
    import { annotationHistory, undo, addHistory } from "$lib/store/annotationHistory";

    export let data: PageData;

    $: isOldest = $annotationHistory.currentIndex === -1;

    onMount(async () => {
        console.log(data)
    });

    const handleUndo = () => {
        const dst = undo($annotationHistory);
        if (dst >= 0) {
            const path = window.location.pathname;
            // FIXME: なぜかinvalidateAllしてもページが更新されないので，リソーそを無理矢理変更している
            data = { ...data, posture: null };
            goto(`${path}?id=${dst}`, { invalidateAll: true });
        }
    }

    const sendAnnotation = async (annotated: AnnotationCreateWithFile) => {
        if (!data.posture?.id || !data.user?.id) {
            return null;
        }
        const token = getToken();
        if (!token || token === "") {
            alert("ログインしてください");
            return;
        }
        const annotationApi = createAnnotationApi({ token: token, basePath: import.meta.env.VITE_API_ENDPOINT });
        const createMode: boolean = $page.url.searchParams.get("id") === null;
        try {
            if (createMode) {
                await annotationApi.createAnnotation({ annotationCreateWithFile: annotated });
            } else {
                await annotationApi.updateAnnotationByPostureIdAndAnnotaterId({
                    postureId: data.posture.id,
                    annotaterId: data.user.id,
                    annotationUpdateWithFile: annotated as AnnotationUpdateWithFile
                });
            }
            addHistory(data.posture.id);
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
    <button on:click={handleUndo} disabled={isOldest}>戻る</button>
    {#if data.posture }
        <PostureAnnotater
            posture={data.posture}
            imageSrc={imageUrlFromPosture(data.posture, "original")}
            handleAction={sendAnnotation}
            showWaist={true}
            holdShoulder={true}
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

        button {
            padding: 8px 16px;
            border: solid 1px #ccc;
        }
    }
</style>