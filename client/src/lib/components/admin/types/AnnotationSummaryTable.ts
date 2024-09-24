export type Header<KEY> = {
	key: KEY;
	display: string;
	type: 'number' | 'string' | 'image';
	digit?: number;
	highlightThreshold?: number;
	clickable?: boolean;
};
