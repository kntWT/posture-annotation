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
export type FilterAvailableType = 'number' | 'string' | 'boolean' | 'date';
export type FilterOption = {
	key: FilterOptionKeys;
	label: FilterOptionLabels;
	availableTypes: FilterAvailableType[];
};
