<script lang="ts">
	import { filterOptions } from './config';

	import { createEventDispatcher } from 'svelte';

	import type { Option } from './types/Option';
	import type { FilterOptionKeys } from './types/FilterOption';

	import IconButton from '@smui/icon-button';
	import DropdownFilter from './DropdownFilter.svelte';
	import Accordion, { Content, Header, Panel } from '@smui-extra/accordion';

	type T = $$Generic;
	type Key = keyof T;
	export let data: T[] = [];
	export let counts: { display: number; total: number };
	export let optionTemplate: Option<Key>[];
	let filters: { value: string; key: Key | undefined; option: Option<Key>[] }[] = [];
	let filterOptionKey: FilterOptionKeys = 'equal';

	const remoteFilter = (i: number) => {
		filters = filters.filter((f, index) => index !== i);
	};
	const addFilter = () => {
		filters = [...filters, { value: '', key: undefined, option: [...optionTemplate] }];
	};

	const dispatch = createEventDispatcher<{ updateData: T[] }>();
	const update = (filtered: T[]) => {
		dispatch('updateData', filtered);
	};

	$: {
		let filtered = [...data];
		filters.forEach((filter) => {
			const option = optionTemplate.find((o) => o.key === filter.key);
			if (!option) return;
			const key = option.key;

			let value: unknown;
			switch (option.type) {
				case 'number':
					value = Number(filter.value);
					break;
				case 'date':
					value = new Date(filter.value).getTime();
					break;
				case 'string':
					value = filter.value;
					break;
			}
			filtered = filtered.filter((d) => {
				const filterOpt = filterOptions.find((f) => f.key === filterOptionKey);
				if (!filterOpt) return false;
				const { key: currentKey } = filterOpt;
				const t = typeof d[key];
				switch (currentKey) {
					case 'above':
						return d[key] > (value as typeof t);
					case 'greater':
						return d[key] >= (value as typeof t);
					case 'below':
						return d[key] < (value as typeof t);
					case 'less':
						return d[key] <= (value as typeof t);
					case 'equal':
						return d[key] === value;
					case 'notEqual':
						return d[key] !== value;
				}
			});
		});
		// NOTE: なぜかsetTimeoutを使わないとfilterの変更がUIに反映されない
		setTimeout(() => update(filtered), 0);
	}
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
