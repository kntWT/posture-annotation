import type { User } from '$lib/api/generated';
import { writable } from 'svelte/store';
import { invalidateAll } from '$app/navigation';

export const user = writable<User|null>(null);

export const login = (newUser: User) => {
    user.set(newUser);
}

export const logout = () => {
    user.set(null);
    invalidateAll();
}