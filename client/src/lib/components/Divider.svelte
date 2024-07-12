<script lang="ts">
    export let content: string = "";
    export let color: string = "black";
    export let width: string = "100%";
</script>

<div style={`background-color: ${color}; width: ${width}`}>{content}</div>

<style lang="scss">
    div {
        color: var(--color, black);
        display: flex;
        align-items: center;
        margin: 0 auto;
        height: 1px;

        &::before, &::after {
            content: "";
            flex: 1;
        }
    }
</style>