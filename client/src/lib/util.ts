import type { Posture } from '$api/generated';

const paths = ['original', 'annotated'] as const;
type Path = (typeof paths)[number];

type DirectoryInfo = {
	userId: number | 'sample';
	annotaterId?: number;
	fileName: string;
};

export const formatDate = (date: Date | undefined) => {
	if (!date) {
		return '';
	}
	const dateStr = date.toLocaleString('ja-JP', { timeZone: 'Asia/Tokyo' });
	const [_, ...parts] = dateStr.match(/(\d+)\/(\d+)\/(\d+) (\d+):(\d+):(\d+)/) ?? [];
	if (!_ && parts.length < 6) {
		return '';
	}
	const [year, month, day, hour, minute, second] = parts.map((p) => parseInt(p));
	const ms = date.getMilliseconds();
	const pad = (num: number, digit: number) => num.toString().padStart(digit, '0');
	return `${year}-${pad(month, 2)}-${pad(day, 2)}_${pad(hour, 2)}:${pad(minute, 2)}:${pad(second, 2)}.${pad(ms, 3)}`;
};

type color = 'green' | 'red' | '';
export const getHighlightColor = (value: number, thres?: number): color => {
	if (!thres) {
		return '';
	}
	if (value > thres) {
		return 'green';
	} else if (value < -thres) {
		return 'red';
	} else {
		return '';
	}
};

export const imageUrl = (info: DirectoryInfo, path: Path = 'original') => {
	const baseDir =
		path === 'original'
			? `${import.meta.env.VITE_FILE_CLIENT_URL}/images/${path}/${info.userId}`
			: `${import.meta.env.VITE_FILE_CLIENT_URL}/images/${path}/${info.userId}/${info.annotaterId ?? 1}`;
	return `${baseDir}/${info.fileName}`;
};

export const imageUrlFromPath = (path: string) => {
	return `${import.meta.env.VITE_FILE_CLIENT_URL}/images/${path}`;
};

export const imageUrlFromPosture = (
	posture: Posture,
	annotaterId: number,
	path: Path = 'original'
) => {
	const info: DirectoryInfo = {
		userId: posture.userId,
		annotaterId,
		fileName: formatDate(posture.exCreatedAt) + '.jpg'
	};
	return imageUrl(info, path);
};

export const toBearer = (token: string) => {
	return `Bearer ${token}`;
};

export const mergeArray = <T>(arr1: T[], arr2: T[], key: keyof T | ((obj: T) => unknown)) => {
	const map = new Map();
	arr1.forEach((item) => map.set(typeof key === 'function' ? key(item) : item[key], item));
	arr2.forEach((item) => map.set(typeof key === 'function' ? key(item) : item[key], item));
	return Array.from(map.values());
};
