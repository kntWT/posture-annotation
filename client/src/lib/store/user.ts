import type { User } from '$api/generated/models';
import { get, writable } from 'svelte/store';

export const user = writable<User|null>(null);

export const login = (newUser: User) => {
    user.set(newUser);
}

export const logout = () => {
    user.set(null);
}

export const isLoggedIn = () => {
    return get(user) !== null;
}

export const getUser = () => {
    return get(user);
}

export const getToken = () => {
    return get(user)?.token;
}