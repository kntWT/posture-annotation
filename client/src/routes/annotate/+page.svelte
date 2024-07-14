<script lang="ts">
    import { onMount } from "svelte";
    import type { PageData } from "./$types";
	import Annotate from "./Annotate.svelte";
	import { goto } from "$app/navigation";

    export let data: PageData;

    onMount(() => {
        console.log(data)
    });

    const handlePostUndo = (dst: number) => {
        const path = window.location.pathname;
        // FIXME: なぜかinvalidateAllしてもページが更新されないので，リソーそを無理矢理変更している
        data = { ...data, posture: null };
        goto(`${path}?id=${dst}`, { invalidateAll: true });
    }

    const onSuccess = () => {
        const path = window.location.pathname;
        goto(path, { invalidateAll: true });
        data = { ...data, posture: null };
    }

    const onError = (e?: Error) => {
        console.error(e);
        // FIXME: なぜか再レンダリングしても破棄されたPostureAnnotaterのsubmitが呼ばれてしまうので，無理矢理リセット
        window.location.href = `/annotate?id=${data.posture?.id}`
        // alert("データの送信に失敗しました");
    }

</script>

<Annotate
    posture={data.posture}
    user={data.user}
    handlePostUndo={handlePostUndo}
    onSuccess={onSuccess}
    onError={onError}
/>