<script lang="ts">
	import type { CheckboxOption, Option } from './types/Option';

	import CheckBox from '@smui/checkbox';
	import FormField from '@smui/form-field';
	import Select, { Option as OptionEl } from '@smui/select';

	type T = $$Generic;
	type Key = keyof T;
	export let options: Option<Key>[] = [];
	export let key: Key | undefined;
	export let checkboxOptions: CheckboxOption[] = [];
	export let checks: Pick<CheckboxOption, 'value'>[];
</script>

<FormField>
	<Select bind:value={key}>
		{#each options as { label, key: _key }}
			<OptionEl value={_key}>{label || ''}</OptionEl>
		{/each}
	</Select>
	{#each checkboxOptions || [] as { label, value }}
		<CheckBox bind:group={checks} {value} />
		<span>{label}</span>
	{/each}
</FormField>
