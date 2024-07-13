<script lang="ts">
    import { onMount } from 'svelte';
    import { logout } from '$lib/store/user';
	import { goto } from '$app/navigation';

    onMount(() => {
        logout();
        goto("/");
    });
</script>