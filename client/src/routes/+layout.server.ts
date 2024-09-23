import type { LayoutServerLoad } from './$types';
import { userApi } from '$api';
import { toBearer } from '$lib/util';

export const load: LayoutServerLoad = async ({ cookies }) => {
	const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
	if (!token || token === '') {
		return {
			user: null
		};
	}

	try {
		const user = await userApi.getUserByToken({ authorization: toBearer(token) });
		return { user };
	} catch (e) {
		return { user: null };
	}
};
