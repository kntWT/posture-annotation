import { userApi } from '$api';
import { toBearer } from '$lib/util';
import { redirect } from '@sveltejs/kit';
import type { LayoutServerLoad } from './$types';

export const load: LayoutServerLoad = async ({ cookies }) => {
	const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
	if (!token || token === '') {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
	}

	try {
		const user = await userApi.getUserByToken({ authorization: toBearer(token) });
		const userId: number = user.id;
		const adminUserIds: number[] = import.meta.env.VITE_ADMIN_USER_IDS.split(',').map(Number);
		const isAdmin = adminUserIds.includes(userId);

		if (!isAdmin) {
			redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
		}
	} catch (e) {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
	}
};
