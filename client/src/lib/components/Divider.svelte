<script lang="ts">
    export let content: string = "";
    export let color: string = "black";
</script>

<div style={`background-color: ${color};`}>{content}</div>

<style lang="scss">
    div {
        color: var(--color, black);
        display: flex;
        align-items: center;
        margin: 0 auto;
        width: 90vw;
        height: 1px;

        &::before, &::after {
            content: "";
            flex: 1;
        }
    }
</style>