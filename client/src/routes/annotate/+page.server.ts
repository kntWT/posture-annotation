import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { createPostureApi, userApi } from "$api";
import { toBearer } from '../../lib/util';


export const load: PageServerLoad = async ({ cookies, url }) => {
    const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
    if (!token || token === '' || token === 'undefined') {
        redirect(301, '/login');
    }
    const id = url.searchParams.get("id") ?? null;
    const postureApi = createPostureApi({ token });
    try {
        if (id && /^[0-9]+$/.test(id)) {
            const posture = await postureApi.getPostureById({ id: parseInt(id) });
            return { posture };
        } else {
            const user = await userApi.getUserByToken({ authorization: toBearer(token) });
            const posture = await postureApi.getRandomPostureByAnnotaterId({ annotaterId: user.id });
            return { posture };
        }
    } catch (e) {
        console.error(e);
        return { posture: null };
    }
}