<script lang="ts">
	import { onMount } from 'svelte';
	import type { PageData } from './$types';
	import Annotate from '$lib/components/annotate/Annotate.svelte';
	import { goto } from '$app/navigation';
	import HelpTip from '$lib/components/common/HelpTip.svelte';
	import Modal from '$lib/components/common/Modal.svelte';
	import HowToAnnotate from '$lib/components/help/HowToAnnotate.svelte';

	export let data: PageData;

	let openHelpModal = false;
	// FIXME: なぜか一度#ifを使ってコンポーネントを再レンダリングしないと送信できなくなる場合がある
	let refreshState = false;
	const base = import.meta.env.VITE_BASE_PATH;

	onMount(() => {
		console.log(data);
	});

	const handleRefresh = () => {
		refreshState = true;
		setTimeout(() => {
			refreshState = false;
		}, 100);
	};

	const handlePostUndo = (dst: number) => {
		const path = window.location.pathname;
		// FIXME: なぜかinvalidateAllしてもページが更新されないので，リソーそを無理矢理変更している
		data = { ...data, posture: null };
		goto(`${path}?id=${dst}`, { invalidateAll: true });
		handleRefresh();
	};

	const onSuccess = () => {
		const path = window.location.pathname;
		goto(path, { invalidateAll: true });
		data = { ...data, posture: undefined };
		handleRefresh();
	};

	const onError = (e?: Error) => {
		console.error(e);
		// FIXME: なぜか再レンダリングしても破棄されたPostureAnnotaterのsubmitが呼ばれてしまうので，無理矢理リセット
		window.location.href = `${base}/annotate?id=${data.posture?.id}`;
		// alert("データの送信に失敗しました");
	};

	const handleOpenHelpModal = () => {
		openHelpModal = true;
	};

	const handleCloseHelpModal = () => {
		openHelpModal = false;
	};

	$: user = openHelpModal ? null : data.user;
</script>

<!-- FIXME: モーダルが開いている時にもp5のmousePressedイベントが発火してしまうので，userをnullにして送信できなくする -->
{#await data.posture}
	<p>loading...</p>
{:then posture}
	{#if posture && user && !refreshState}
		{#key user}
			<Annotate {posture} {user} {handlePostUndo} {onSuccess} {onError} />
		{/key}
	{:else if posture === null}
		<p>
			<strong>お疲れ様です，全てのデータをアノテーションしました！</strong>
		</p>
	{/if}
{/await}
<Modal open={openHelpModal} handleClose={handleCloseHelpModal}>
	<HowToAnnotate topic="" showTableOfContents={false} />
</Modal>
<HelpTip handleClick={handleOpenHelpModal} />

<style lang="scss">
	p {
		margin-top: 24px;
		width: 100%;
		text-align: center;
	}
</style>
