<script lang="ts">
	import type { Option } from './types/Option';

	import FormField from '@smui/form-field';
	import Select, { Option as OptionEl } from '@smui/select';

	type T = $$Generic;
	type KEY = keyof T;
	export let options: Option<KEY>[] = [];
	export let key: KEY | undefined;
	export let value: 'asc' | 'desc' = 'asc';

	const sortOptions = [
		{ label: '昇順', key: 'asc' },
		{ label: '降順', key: 'desc' }
	];
</script>

<FormField>
	<Select bind:value={key}>
		{#each options as { label, key: _key }}
			<OptionEl value={_key}>{label || ''}</OptionEl>
		{/each}
	</Select>
	&nbsp;
	<Select bind:value>
		{#each sortOptions as { label, key: _key }}
			<OptionEl value={_key}>{label}</OptionEl>
		{/each}
	</Select>
</FormField>

<style lang="scss" scoped>
	:global(.mdc-form-field) {
		gap: 3rem;
	}
</style>
