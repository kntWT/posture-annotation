<script lang="ts">
    import Dialog, { Actions, Header } from "@smui/dialog";
    import IconButton from "@smui/icon-button";
	import Paper from "@smui/paper";

    export let open: boolean;
    export let handleClose: () => void;

</script>

{#if open}
    <Dialog
        bind:open
        on:SMUIDialog:closed={handleClose}
    >
        <Paper>
            <Header>
                <Actions>
                    <IconButton on:click={handleClose} class="material-icons">close</IconButton>
                </Actions>
            </Header>
            <slot />
        </Paper>
    </Dialog>
{/if}

<style lang="scss" scoped>
    :global(.mdc-dialog) {
        :global( .mdc-icon-button){
            position: absolute;
            right: 0;
            top: 0;
            margin: 16px;
        }

        :global(.mdc-dialog__container) {
            width: 90%;
            max-width: 1200px;

            :global(.mdc-dialog__surface) {
                width: 100%;
                max-width: 1200px;
            }
        }

        // :global(.smui-paper) {
        //     width: 80%;
        //     max-width: 800px;
        // }
    }
</style>
