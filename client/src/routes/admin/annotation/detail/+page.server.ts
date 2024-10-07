import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';
import { createPostureApi } from '$api';

export const load: PageServerLoad = async ({ url, locals }) => {
	const postureId: number = parseInt(url.searchParams.get('posture_id') || '0');
	if (postureId <= 0) {
		redirect(301, `${import.meta.env.VITE_BASE_PATH}/admin/annotation`);
	}

	try {
		const postureApi = createPostureApi({ token: locals.user.token });
		const data = await postureApi.getPostureWithAnnotationsById({ id: postureId });
		return { data };
	} catch (e) {
		return { data: null };
	}
};
