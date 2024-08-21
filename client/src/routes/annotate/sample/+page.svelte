<script lang="ts">
	import { onMount } from 'svelte';
	import type { PageData } from './$types';
	import Annotate from '$lib/components/annotate/Annotate.svelte';
	import { goto } from '$app/navigation';
	import HelpTip from '$lib/components/common/HelpTip.svelte';
	import Modal from '$lib/components/common/Modal.svelte';
	import HowToAnnotate from '$lib/components/help/HowToAnnotate.svelte';
	import Button from '@smui/button';

	export let data: PageData;

	let openHelpModal = false;
	const base = import.meta.env.VITE_BASE_PATH;

	onMount(() => {
		console.log(data);
	});

	const handlePostUndo = (dst: number) => {
		const path = window.location.pathname;
		// FIXME: なぜかinvalidateAllしてもページが更新されないので，リソーそを無理矢理変更している
		data = { ...data, posture: null };
		goto(`${path}?id=${dst}`, { invalidateAll: true });
	};

	const onSuccess = () => {
		const path = window.location.pathname;
		goto(path, { invalidateAll: true });
		data = { ...data, posture: null };
	};

	const onError = (e?: Error) => {
		console.error(e);
		// FIXME: なぜか再レンダリングしても破棄されたPostureAnnotaterのsubmitが呼ばれてしまうので，無理矢理リセット
		window.location.href = `${base}/annotate/sample?id=${data.posture?.id}`;
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
{#if data.posture && data.user && !openHelpModal}
	{#key user}
		<Annotate posture={data.posture} {user} {handlePostUndo} {onSuccess} {onError} />
	{/key}
{:else if data.posture === null}
	<div>
		<div>
			<strong>お疲れ様です，全てのデータをアノテーションしました！</strong><br />
			<strong>本番用のアノテーションに取り組んでください！</strong><br />
		</div>
		<Button variant="raised" color="primary" href="{base}/annotate">本番用アノテーション</Button>
	</div>
{/if}
<Modal open={openHelpModal} handleClose={handleCloseHelpModal}>
	<HowToAnnotate topic="" showTableOfContents={false} />
</Modal>
<HelpTip handleClick={handleOpenHelpModal} />

<style lang="scss" scoped>
	div {
		margin-top: 24px;
		text-align: center;
		width: 100%;

		:global(.mdc-button) {
			margin-top: 24px;
		}
	}
</style>
