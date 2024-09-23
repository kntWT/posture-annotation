<script lang="ts">
	import { ValidationMessage } from '@felte/reporter-svelte';

	export let name: string;
</script>

<ValidationMessage for={name} let:messages={message}>
	<div class="error-info">{message || ''}</div>
</ValidationMessage>

<style lang="scss" scoped>
	@import '$lib/styles/_variables';
	.error-info {
		color: $error-color;
		font-size: 0.8rem;
	}
</style>
