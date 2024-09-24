export type Header<KEY> = {
	key: KEY;
	display: string;
	isNumeric: boolean;
	digit?: number;
	highlightThreshold?: number;
};
