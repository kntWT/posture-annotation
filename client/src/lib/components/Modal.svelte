<script lang="ts">
    export let open: boolean = false;
    export let handleClose: () => void;
</script>

{#if open}
    <div
        class="modal"
        on:click={handleClose}
    >
        <slot />
    </div>
{/if}

<style lang="scss">
    .modal {
        position: fixed;
        z-index: 100;
        left: 0;
        top: 0;
        width: 100vw;
        height: 100vh;
        overflow: scroll;
        background-color: rgba(0, 0, 0, 0.4);
    }
</style>
