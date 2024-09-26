export type Option<KEY> = {
	label: string;
	key: KEY;
	type: 'number' | 'string' | 'boolean' | 'date';
};
