import { createAnnotationApi } from '$api';
import type { PageServerLoad } from './$types';
import { redirect } from '@sveltejs/kit';

export const load: PageServerLoad = async ({ cookies }) => {
	const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
	if (!token || token === '') {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
	}

	try {
		const annotationApi = createAnnotationApi({ token });
		const data = await annotationApi.getAnnotationSummaryByAnnotater();
		return { data };
	} catch (e) {
		return { data: null };
	}
};
