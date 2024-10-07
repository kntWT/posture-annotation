import type { PageServerLoad } from './$types';
import { createPostureApi, userApi } from '$api';
import { toBearer } from '$lib/util';

export const load: PageServerLoad = async ({ locals, url }) => {
	const token = locals.user.token;
	const id = url.searchParams.get('id') ?? null;
	const postureApi = createPostureApi({ token });
	try {
		if (id && /^[0-9]+$/.test(id)) {
			const posture = await postureApi.getPostureById({ id: parseInt(id) });
			return { posture };
		} else {
			const user = await userApi.getUserByToken({ authorization: toBearer(token) });
			const posture = await postureApi.getRandomSamplePostureByAnnotaterId({
				annotaterId: user.id
			});
			return { posture };
		}
	} catch (e) {
		console.error(e);
		return { posture: null };
	}
};
