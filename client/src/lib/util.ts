import type { Posture } from "$api/generated";

const paths = ["original", "annotated"] as const;
type Path = typeof paths[number];

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

export const imageUrl = (userId: number, fileName: string, path: Path = "original") => {
    return `${import.meta.env.VITE_FILE_SERVER_ENDPOINT}/images/${path}/${userId}/${fileName}`;
}

export const imageUrlFromPosture = (posture: Posture, path: Path = "original") => {
    return imageUrl(posture.userId, formatDate(posture.exCreatedAt) + ".jpg", path);
}

export const toBearer = (token: string) => {
    return `Bearer ${token}`;
}