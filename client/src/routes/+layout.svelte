<script lang="ts">
	import Header from '$lib/components/common/Header.svelte';
    import { onMount } from 'svelte';
    import type { LayoutData } from './$types';
	import { login } from '$lib/store/user';

    export let data: LayoutData;
    onMount(() => {
        if (data.user) {
            login(data.user);
        }
    });
</script>

<div class="app">
	<Header />

	<main>
		<slot />
	</main>
</div>

<style lang="scss" scoped>
    @import "$lib/styles/global";

    .app {
        @include html;
    }
</style>