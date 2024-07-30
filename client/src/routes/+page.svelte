<script lang="ts">
	import { page } from "$app/stores";
	import About from "$lib/components/home/About.svelte";
    import HowToAnnotate from "$lib/components/help/HowToAnnotate.svelte";
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
