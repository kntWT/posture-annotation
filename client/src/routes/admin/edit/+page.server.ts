import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { createAnnotationApi } from '$api';

export const load: PageServerLoad = async ({ cookies, url }) => {
	const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
	if (!token || token === '') {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
	}

	const id: number = parseInt(url.searchParams.get('annotation_id') || '0');
	if (id <= 0) {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/admin`);
	}

	try {
		const postureApi = createAnnotationApi({ token });
		const data = await postureApi.getAnnotationWithPostureById({ id });
		return { data };
	} catch (e) {
		return { data: null };
	}
};
