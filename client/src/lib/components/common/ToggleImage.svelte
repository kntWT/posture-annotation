<script lang="ts">
	import Button, { Label } from '@smui/button';
	import { Icon } from '@smui/icon-button';

	export let src: string;
	export let alt: string = '';
	export let viewBox: { top: number; right: number; bottom: number; left: number } = {
		top: 0,
		right: 0,
		bottom: 0,
		left: 0
	};
	export const showDefault: boolean = false;

	let show = showDefault;
</script>

<div>
	{#if show}
		<Button variant="outlined" on:click={() => (show = false)}>
			<Label>画像を非表示</Label>
			<Icon class="material-icons">arrow_drop_up</Icon>
		</Button>
		<br />
		<img
			{src}
			{alt}
			style={`object-view-box: inset(${viewBox.top}px ${viewBox.right}px ${viewBox.bottom}px ${viewBox.left}px);`}
		/>
	{:else}
		<Button variant="outlined" on:click={() => (show = true)}>
			<Label>画像を表示</Label>
			<Icon class="material-icons">arrow_drop_down</Icon>
		</Button>
	{/if}
</div>
