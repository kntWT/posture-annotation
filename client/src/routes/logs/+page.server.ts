import type { PageServerLoad } from './$types';
import { createAnnotationApi, userApi } from '$api';
import { toBearer } from '$lib/util';

export const load: PageServerLoad = async ({ locals }) => {
	const token = locals.user.token;
	const annotationApi = createAnnotationApi({ token });
	try {
		const user = await userApi.getUserByToken({ authorization: toBearer(token) });
		if (!user) {
			return { prod: null, sample: null };
		}
		const param = { annotaterId: user.id };
		const prods = await annotationApi.getProdAnnotationsWithPostureByAnnotaterId(param);
		const prodCount = await annotationApi.getProdAnnotationCountByAnnotaterId(param);
		const samples = await annotationApi.getSampleAnnotationsWithPostureByAnnotaterId(param);
		const sampleCount = await annotationApi.getSampleAnnotationCountByAnnotaterId(param);

		return {
			prod: { annotations: prods, count: prodCount },
			sample: { annotations: samples, count: sampleCount }
		};
	} catch (e) {
		console.error(e);
		return { prod: null, sample: null };
	}
};
