<script lang="ts">
	import IconButton from '@smui/icon-button';

	export let src: string;
	export let alt: string = '';
	export let width: number | string = 100;
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
		画像を非表示
		<IconButton class="material-icons" on:click={() => (show = false)}>arrow_drop_up</IconButton>
		<br />
		<img
			{src}
			{alt}
			{width}
			style={`object-view-box: inset(${viewBox.top}px ${viewBox.right}px ${viewBox.bottom}px ${viewBox.left}px);`}
		/>
	{:else}
		画像を表示
		<IconButton class="material-icons" on:click={() => (show = true)}>arrow_drop_down</IconButton>
	{/if}
</div>
