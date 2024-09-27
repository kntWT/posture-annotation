<script lang="ts">
	import type { FilterOption, FilterOptionKeys } from './types/Option';

	import type { Option } from './types/Option';

	import FormField from '@smui/form-field';
	import Textfield from '@smui/textfield';
	import Select, { Option as OptionEl } from '@smui/select';

	type T = $$Generic;
	type KEY = keyof T;
	export let options: Option<KEY>[] = [];
	export let key: KEY | undefined;
	export let value: string;
	export let filterOptions: FilterOption[];
	export let filterOptionKey: FilterOptionKeys = 'equal';

	$: availableOptions = filterOptions.filter((e) => {
		const option = options.find((o) => o.key === key);
		return option && e.availableTypes.includes(option.type);
	});
</script>

<FormField>
	<Select bind:value={key}>
		{#each options as { label, key: _key }}
			<OptionEl value={_key}>{label || ''}</OptionEl>
		{/each}
	</Select>
	が
	<Textfield bind:value />
	&nbsp;
	<Select bind:value={filterOptionKey}>
		{#each availableOptions as { label, key: _key }}
			<OptionEl value={_key}>{label}</OptionEl>
		{/each}
	</Select>
</FormField>

<style lang="scss" scoped>
	:global(.mdc-form-field) {
		gap: 3rem;
	}
</style>
