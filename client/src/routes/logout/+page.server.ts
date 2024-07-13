import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async ({ cookies }) => {
    cookies.set(`${import.meta.env.VITE_COOKIE_PREFIX}token`, '', { path: "/" });
}