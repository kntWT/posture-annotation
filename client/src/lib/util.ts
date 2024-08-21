import type { Posture } from "$api/generated";

const paths = ["original", "annotated"] as const;
type Path = typeof paths[number];

type DirectoryInfo = {
    userId: number | "sample";
    annotaterId?: number;
    fileName: string;
}

const base = import.meta.env.VITE_BASE_PATH;

export const formatDate = (date: Date | undefined) => {
    if (!date) {
        return "";
    }
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    const hour = date.getHours();
    const minute = date.getMinutes();
    const second = date.getSeconds();
    const ms = date.getMilliseconds();
    const pad = (num: number, digit: number) => num.toString().padStart(digit, "0");
    return `${year}-${pad(month, 2)}-${pad(day, 2)}_${pad(hour, 2)}:${pad(minute, 2)}:${pad(second, 2)}.${pad(ms, 3)}`;
}

export const imageUrl = (info: DirectoryInfo, path: Path = "original") => {
    const baseDir = path === "original"
        ? `${base}/${import.meta.env.VITE_FILE_SERVER_ENDPOINT}/images/${path}/${info.userId}`
        : `${base}/${import.meta.env.VITE_FILE_SERVER_ENDPOINT}/images/${path}/${info.userId}/${info.annotaterId ?? 1}`;
    return `${baseDir}/${info.fileName}`;
}

export const imageUrlFromPosture = (posture: Posture, annotaterId: number, path: Path = "original") => {
    const info: DirectoryInfo = {
        userId: posture.userId,
        annotaterId,
        fileName: formatDate(posture.exCreatedAt) + ".jpg"
    };
    return imageUrl(info, path);
}

export const toBearer = (token: string) => {
    return `Bearer ${token}`;
}
