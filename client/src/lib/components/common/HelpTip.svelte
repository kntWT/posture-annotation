<script lang="ts">
    import IconButton from "@smui/icon-button";

    export let handleClick: () => void;

</script>

<div class="wrapper">
    <IconButton class="material-icons" on:click={handleClick}>
        help
    </IconButton>
</div>

<style lang="scss" scoped>
    .wrapper {
        :global(.mdc-icon-button) {
            position: fixed;
            top: auto;
            bottom: 0;
            right: 0;
            left: auto;
            margin: 16px;
        }
    }
</style>
