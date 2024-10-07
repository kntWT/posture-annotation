import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { createAnnotationApi } from '$api';

export const load: PageServerLoad = async ({ locals, url }) => {
	const annotaterId: number = parseInt(url.searchParams.get('annotater_id') || '0');
	if (annotaterId <= 0) {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/admin/user`);
	}

	try {
		const postureApi = createAnnotationApi({ token: locals.user.token });
		const data = await postureApi.getAnnotationsWithPostureByAnnotaterId({ annotaterId });
		return { data };
	} catch (e) {
		return { data: null };
	}
};
