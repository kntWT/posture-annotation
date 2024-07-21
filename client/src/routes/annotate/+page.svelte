<script lang="ts">
    import { onMount } from "svelte";
    import type { PageData } from "./$types";
	import Annotate from "./Annotate.svelte";
	import { goto } from "$app/navigation";
	import HelpTip from "$lib/components/HelpTip.svelte";
	import Modal from "$lib/components/Modal.svelte";
	import HowToAnnotate from "$lib/components/HowToAnnotate.svelte";

    export let data: PageData;

    let openHelpModal = false;

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

    const handleOpenHelpModal = () => {
        openHelpModal = true;
    }

    const handleCloseHelpModal = () => {
        openHelpModal = false;
    }

    $: user = openHelpModal ? null : data.user;

</script>

<!-- FIXME: モーダルが開いている時にもp5のmousePressedイベントが発火してしまうので，userをnullにして送信できなくする -->
{#key user}
<Annotate
    posture={data.posture}
    user={user}
    handlePostUndo={handlePostUndo}
    onSuccess={onSuccess}
    onError={onError}
/>
{/key}
<Modal open={openHelpModal} handleClose={handleCloseHelpModal}>
    <HowToAnnotate />
</Modal>
<HelpTip handleClick={handleOpenHelpModal} />
