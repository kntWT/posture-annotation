export type FilterOptionLabels =
	| '以上'
	| 'より大きい'
	| '以下'
	| 'より小さい'
	| 'を含む'
	| 'と等しい'
	| 'と等しくない';
export type FilterOptionKeys =
	| 'above'
	| 'greater'
	| 'below'
	| 'less'
	| 'include'
	| 'equal'
	| 'notEqual';
export const isFilterOptionKeys = (value: unknown): value is FilterOptionKeys =>
	value === 'above' ||
	value === 'greater' ||
	value === 'below' ||
	value === 'less' ||
	value === 'include' ||
	value === 'equal' ||
	value === 'notEqual';
export type FilterAvailableType = 'number' | 'string' | 'boolean' | 'date';
export type FilterAvailableUIType = 'dropdown' | 'checkbox';
export type CheckboxOption = {
	label: string;
	value: 'true' | 'false';
};
export type Option<Key> = {
	label: string;
	key: Key;
	type: 'number' | 'string' | 'boolean' | 'date';
	availableUiTypes: FilterAvailableUIType[];
	checkboxConfigs?: CheckboxOption[];
};
export type FilterOption = {
	key: FilterOptionKeys;
	label: FilterOptionLabels;
	availableTypes: FilterAvailableType[];
};
