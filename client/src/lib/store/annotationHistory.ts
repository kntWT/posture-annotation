import { get, writable } from 'svelte/store';

export type AnnotationHistory = {
    history: number[];
    currentIndex: number;
}

export const annotationHistory = writable<AnnotationHistory>({
    history: [],
    currentIndex: -1
});

export const addHistory = (id: number) => {
    const ah = get(annotationHistory);
    const history = ah.history.includes(id) ? [...ah.history] : [...ah.history, id];
    const currentIndex = history.length - 1;
    annotationHistory.set({ history, currentIndex });
}

export const clearAnnotationHistory = () => {
    annotationHistory.set({ history: [], currentIndex: -1 });
}

export const getIsOldest = (): boolean => {
    const ah = get(annotationHistory);
    return ah.currentIndex === 0;
}

export const getIsNewest = (): boolean => {
    const ah = get(annotationHistory);
    return ah.currentIndex === ah.history.length - 1;
}

export const getHistory = (): number[] => {
    return get(annotationHistory).history;
}

export const getCurrentIndex = (): number => {
    return get(annotationHistory).currentIndex;
}

export const undo = (ah: AnnotationHistory): number => {
    const history = ah.history;
    if (history.length === 0) {
        return -1;
    }
    const currentIndex = ah.currentIndex;
    annotationHistory.set({ history, currentIndex: currentIndex -1 });
    return history[currentIndex];
}

export const redo = (ah: AnnotationHistory): number => {
    const history = ah.history;
    const currentIndex = ah.currentIndex + 1;
    if (currentIndex === history.length) {
        return -1;
    }
    annotationHistory.set({ history, currentIndex: currentIndex });
    return history[currentIndex];
}