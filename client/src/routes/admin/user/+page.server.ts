import { createAnnotationApi } from '$api';
import type { PageServerLoad } from './$types';

export const load: PageServerLoad = async ({ locals }) => {
	try {
		const annotationApi = createAnnotationApi({ token: locals.user.token });
		const data = await annotationApi.getAnnotationSummaryByAnnotater();
		return { data };
	} catch (e) {
		return { data: null };
	}
};
