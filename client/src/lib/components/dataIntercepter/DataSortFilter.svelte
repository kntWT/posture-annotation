<script lang="ts">
	import { filterOptions } from './config';
	import { createEventDispatcher } from 'svelte';
	import type { CheckboxOption, Option } from './types/Option';
	import {
		isFilterOptionKeys,
		type FilterAvailableUIType,
		type FilterOptionKeys
	} from './types/Option';

	import Button, { Icon } from '@smui/button';
	import IconButton from '@smui/icon-button';
	import CheckboxFilter from './CheckboxFilter.svelte';
	import DropdownFilter from './DropdownFilter.svelte';
	import Accordion, { Content, Header, Panel } from '@smui-extra/accordion';
	import Tooltip, { Wrapper, RichActions } from '@smui/tooltip';
	import DropdownSort from './DropdownSort.svelte';

	type T = $$Generic;
	type Key = keyof T;
	export let data: T[] = [];
	export let counts: { display: number; total: number };
	export let optionTemplate: Option<Key>[];
	type Filter = {
		type: FilterAvailableUIType;
		value: string;
		key: Key | undefined;
		option: Option<Key>[];
		result: FilterOptionKeys | Pick<CheckboxOption, 'value'>[];
	};
	let filters: Filter[] = [];

	let sortKey: Key | undefined;
	let sortValue: 'asc' | 'desc' = 'asc';

	const previous: { filters: Filter[]; sortKey: Key | undefined; sortValue: 'asc' | 'desc' } = {
		filters: [...filters],
		sortKey: sortKey,
		sortValue: sortValue
	};

	const isEqualFilter = (a: Filter, b: Filter) => {
		if (a === undefined || b === undefined) return false;
		return a.type === b.type && a.value === b.value && a.key === b.key && a.result === b.result;
	};

	const remoteFilter = (i: number) => {
		filters = filters.filter((f, index) => index !== i);
	};
	const addFilter = (type: FilterAvailableUIType) => {
		filters = [
			...filters,
			{
				type,
				value: '',
				key: undefined,
				option: [...optionTemplate.filter((o) => o.availableUiTypes.includes(type))],
				result: type === 'dropdown' ? 'equal' : []
			}
		];
	};

	const filterByDropdown = (toFilter: T[], filter: Filter, key: Key, value: unknown) => {
		if (filter.type !== 'dropdown') return toFilter;
		return toFilter.filter((d) => {
			const filterOpt = filterOptions.find((f) => f.key === filter.result);
			if (!filterOpt) return false;
			const { key: currentKey } = filterOpt;
			const t = typeof d[key];
			const v = typeof d[key] === 'number' ? Math.abs(d[key]) : d[key];
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
	};

	const filterByCheckbox = (toFilter: T[], filter: Filter, key: Key) => {
		if (filter.type !== 'checkbox') return toFilter;
		if (isFilterOptionKeys(filter.result)) return toFilter;
		if (!Array.isArray(filter.result)) return toFilter;

		return toFilter.filter((d) => {
			return filter.result.includes(`${d[key]}`.toLowerCase());
		});
	};

	const dispatch = createEventDispatcher<{ updateData: T[] }>();
	const update = (filtered: T[]) => {
		dispatch('updateData', filtered);
	};

	$: checkboxOption = (filter: Filter) => {
		if (filter.type !== 'checkbox') return [];
		const optionIndex = filter.option.findIndex((o) => o.key === filter.key);
		if (optionIndex !== -1) {
			return filter.option[optionIndex].checkboxConfigs;
		}
		return [];
	};

	$: if (!filters.every((f, i) => isEqualFilter(f, previous.filters[i]))) {
		let processed = [...data];
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
			switch (filter.type) {
				case 'dropdown':
					processed = filterByDropdown(processed, filter, key, value);
					break;
				case 'checkbox':
					processed = filterByCheckbox(processed, filter, key);
					break;
			}
		});
		update(processed);
		previous.filters = filters.map((f) => ({ ...f }));
	}

	$: if (sortKey !== previous.sortKey || sortValue !== previous.sortValue) {
		let processed = [...data];
		processed = processed.sort((a, b) => {
			if (!sortKey) return 0;
			if (sortValue === 'asc') {
				return a[sortKey] > b[sortKey] ? 1 : -1;
			} else {
				return a[sortKey] < b[sortKey] ? 1 : -1;
			}
		});
		update(processed);
		previous.sortKey = sortKey;
		previous.sortValue = sortValue;
	}
</script>

<div class="container">
	<Accordion>
		<Panel>
			<Header>
				<p class="center">{counts.display}/{counts.total}件表示</p>
			</Header>
			<Content>
				<div class="sort-container">
					<h4>ソート条件</h4>
					<DropdownSort bind:options={optionTemplate} bind:key={sortKey} bind:value={sortValue} />
				</div>
				<div class="filter-container">
					<h4>フィルター条件</h4>
					{#each filters as filter, i}
						<div class="center filter-container">
							{#if filter.type === 'checkbox' && Array.isArray(filter.result)}
								<CheckboxFilter
									bind:options={filter.option}
									bind:key={filter.key}
									bind:checks={filter.result}
									checkboxOptions={checkboxOption(filter)}
								/>
							{:else if filter.type === 'dropdown' && (isFilterOptionKeys(filter.result) || filter.result === undefined)}
								<DropdownFilter
									bind:options={filter.option}
									bind:key={filter.key}
									bind:value={filter.value}
									bind:filterOptionKey={filter.result}
									{filterOptions}
								/>
							{:else}
								<p>some error occured</p>
							{/if}
							<IconButton class="material-icons" on:click={() => remoteFilter(i)}>remove</IconButton
							>
						</div>
					{/each}
				</div>
				<div class="center add-button-wrapper">
					<Wrapper rich>
						<Icon class="material-icons">add</Icon>
						<Tooltip xPos="center">
							<RichActions>
								<Button on:click={() => addFilter('dropdown')}><span>dropdown</span></Button>
								<Button on:click={() => addFilter('checkbox')}><span>checkbox</span></Button>
							</RichActions>
						</Tooltip>
					</Wrapper>
				</div>
			</Content>
		</Panel>
	</Accordion>
</div>

<style lang="scss" scoped>
	.container {
		.center {
			margin: auto 0;
			text-align: center;
		}

		.filter-container {
			margin-top: 16px;
		}

		:global(.smui-accordion) {
			z-index: 2;
		}

		.filter-container {
			padding: 16px;
		}

		.add-button-wrapper {
			margin-top: 16px;
		}
	}
</style>
