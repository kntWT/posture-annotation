import type { FilterOption } from './types/FilterOption';

export const filterOptions: FilterOption[] = [
	{ key: 'above', label: 'より大きい', availableTypes: ['number', 'date'] },
	{ key: 'greater', label: '以上', availableTypes: ['number', 'date'] },
	{ key: 'below', label: 'より小さい', availableTypes: ['number', 'date'] },
	{ key: 'less', label: '以下', availableTypes: ['number', 'date'] },
	{ key: 'include', label: 'を含む', availableTypes: ['string'] },
	{ key: 'equal', label: 'と等しい', availableTypes: ['number', 'date', 'string'] },
	{ key: 'notEqual', label: 'と等しくない', availableTypes: ['number', 'date', 'string'] }
];
