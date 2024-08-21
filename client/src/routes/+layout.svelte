<script lang="ts">
	import Header from '$lib/components/common/Header.svelte';
	import { onMount } from 'svelte';
	import type { LayoutData } from './$types';
	import { login } from '$lib/store/user';

	export let data: LayoutData;
	const base = import.meta.env.VITE_BASE_PATH;
	onMount(() => {
		if (data.user) {
			login(data.user);
		}
	});
</script>

<svelte:head>
	<link rel="stylesheet" href="{base}/smui.css" media="(prefers-color-scheme: light)" />
	<link
		rel="stylesheet"
		href="{base}/smui-dark.css"
		media="screen and (prefers-color-scheme: dark)"
	/>
</svelte:head>
<div class="app">
	<Header />

	<main>
		<slot />
	</main>
</div>

<style lang="scss" scoped>
	@import '$lib/styles/global';

	.app {
		@include html;
	}
</style>
