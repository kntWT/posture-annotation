<script lang="ts">
	import type { Option } from './types/Option';
	import type { FilterOption, FilterOptionKeys } from './types/FilterOption';

	import IconButton from '@smui/icon-button';
	import DropdownFilter from './DropdownFilter.svelte';
	import Accordion, { Content, Header, Panel } from '@smui-extra/accordion';

	type T = $$Generic;
	type KEY = keyof T;
	export let counts: { display: number; total: number };
	export let optionTemplate: Option<KEY>[];
	export let filters: { value: string; key: KEY | undefined; option: Option<KEY>[] }[] = [];
	export let filterOptions: FilterOption[] = [];
	export let filterOptionKey: FilterOptionKeys = 'equal';

	const remoteFilter = (i: number) => {
		filters = filters.filter((f, index) => index !== i);
	};
	const addFilter = () => {
		filters = [...filters, { value: '', key: undefined, option: [...optionTemplate] }];
	};
</script>

<Accordion>
	<Panel>
		<Header>
			<p class="center">{counts.display}/{counts.total}件表示</p>
		</Header>
		<Content>
			{#each filters as filter, i}
				<div class="center">
					<DropdownFilter
						bind:options={filter.option}
						bind:key={filter.key}
						bind:value={filter.value}
						{filterOptions}
						bind:filterOptionKey
					/>
					<IconButton class="material-icons" on:click={() => remoteFilter(i)}>remove</IconButton>
				</div>
			{/each}
			<div class="center">
				<IconButton class="material-icons" on:click={addFilter}>add</IconButton>
			</div>
		</Content>
	</Panel>
</Accordion>

<style lang="scss" scoped>
	.center {
		margin: auto 0;
		text-align: center;
	}

	:global(.smui-accordion) {
		z-index: 50;
	}
</style>
