import type { PageServerLoad } from './$types';
import { createPostureApi, userApi } from '$api';
import { toBearer } from '$lib/util';
import type { Posture } from '$api/generated';

export const load: PageServerLoad = async ({
	locals,
	url
}): Promise<{ posture: Posture | null | undefined }> => {
	const token = locals.user.token;
	const id = url.searchParams.get('id') ?? null;
	const postureApi = createPostureApi({ token });
	try {
		if (id && /^[0-9]+$/.test(id)) {
			const posture = await postureApi.getPostureById({ id: parseInt(id) });
			return { posture };
		} else {
			const user = await userApi.getUserByToken({ authorization: toBearer(token) });
			const posture = await postureApi.getRandomPostureByAnnotaterIdThinOutById({
				annotaterId: user.id,
				step: import.meta.env.VITE_THIN_OUT_POSTURE_STEP
			});
			return { posture };
		}
	} catch (e) {
		console.error(e);
		return { posture: null };
	}
};
