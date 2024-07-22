<script lang="ts">
	import { page } from "$app/stores";
	import About from "$lib/components/About.svelte";
    import HowToAnnotate from "$lib/components/HowToAnnotate.svelte";
	import { onMount } from "svelte";

    let topic = "";

    onMount(() => {
        topic = $page.url.hash?.substring(1) ?? "";
    });
</script>

<div class="wrapper">
    <About />
    {#key topic}
        <HowToAnnotate topic={topic} />
    {/key}
</div>

<style lang="scss" scoped>
    .wrapper {
        padding: 24px;
    }
</style>
