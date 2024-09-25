<script lang="ts">
	import Button, { Label } from '@smui/button';
	import { Icon } from '@smui/icon-button';

	export let src: string;
	export let alt: string = '';
	export let width: number = 200;
	export let viewBox: { top: number; right: number; bottom: number; left: number } = {
		top: 0,
		right: 0,
		bottom: 0,
		left: 0
	};
	export const showDefault: boolean = false;

	let show = showDefault;
</script>

<div class="wrapper">
	<Button variant="outlined" on:click={() => (show = !show)}>
		<Label>{show ? '画像を非表示' : '画像を非表示'}</Label>
		<Icon class="material-icons">{show ? 'arrow_drop_up' : 'arrow_drop_down'}</Icon>
	</Button>
	<img
		class={`toggle-image ${show ? 'visible' : 'invisible'}`}
		width={`${width}px`}
		{src}
		{alt}
		style={`object-view-box: inset(${viewBox.top}% ${viewBox.right}% ${viewBox.bottom}% ${viewBox.left}%);`}
	/>
</div>

<style lang="scss" scoped>
	.wrapper {
		padding: 8px;
		display: flex;
		flex-direction: column;
		align-items: center;

		.toggle-image {
			margin-top: 8px;

			&.visible {
				display: block;
			}

			&.invisible {
				display: none;
			}
		}
	}
</style>
