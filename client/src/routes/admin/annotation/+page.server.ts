import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { createAnnotationApi } from '$api';

export const load: PageServerLoad = async ({ cookies }) => {
	const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
	if (!token || token === '') {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/login`);
	}

	try {
		const annotationApi = createAnnotationApi({ token });
		const data = await annotationApi.getAnnotationSummaryByPosture();
		return { data };
	} catch (e) {
		console.log(e);
		return { data: null };
	}
};
