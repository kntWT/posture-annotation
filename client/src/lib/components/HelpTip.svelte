<script lang="ts">
    import IconButton from "@smui/icon-button";

    export let handleClick: () => void;

</script>

<div class="wrapper">
    <IconButton class="material-icons" on:click={handleClick}>
        help
    </IconButton>
</div>

<style lang="scss">
    .wrapper {
        width: 100vw;
        :global(.mdc-icon-button) {
            position: absolute;
            left: auto;
            right: 0;
            bottom: 0;
            margin: 16px;
        }
    }
</style>
