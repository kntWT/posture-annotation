<script lang="ts">
	import Header from './Header.svelte';
    import { setContext } from 'svelte';
    import type { LayoutData } from './$types';
	import { login } from '$lib/store/user';

    export let data: LayoutData;
    if (data.user) {
        login(data.user);
        setContext('user', data.user);
    }
</script>

<div class="app">
	<Header />

	<main>
		<slot />
	</main>
</div>

<style lang="scss">
    @import "$lib/styles/global";

    .app {
        @include html;
    }
</style>