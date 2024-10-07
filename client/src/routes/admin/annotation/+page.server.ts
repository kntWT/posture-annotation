import type { PageServerLoad } from './$types';
import { createAnnotationApi } from '$api';

export const load: PageServerLoad = async ({ locals }) => {
	try {
		const annotationApi = createAnnotationApi({ token: locals.user.token });
		const data = await annotationApi.getAnnotationSummaryByPosture();
		return { data };
	} catch (e) {
		console.log(e);
		return { data: null };
	}
};
