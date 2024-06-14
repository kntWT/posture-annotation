import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { createPostureApi, userApi } from "$api";
import { toBearer } from '../../lib/util';



export const load: PageServerLoad = async ({ cookies }) => {
    const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
    if (!token || token === '' || token === 'undefined') {
        redirect(301, '/');
    }

    const postureApi = createPostureApi({ token });
    try {
        const user = await userApi.getUserByToken({ authorization: toBearer(token) });
        const postures = await postureApi.getPosturesByAnnotaterId({ annotaterId: user.id });
        return { postures };
    } catch (e) {
        console.error(e);
        return { postures: [] };
    }
}