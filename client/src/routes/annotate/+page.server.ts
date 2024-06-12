import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { createPostureApi } from "$api";


export const load: PageServerLoad = async ({ cookies }) => {
    const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
    if (!token || token === '' || token === 'undefined') {
        redirect(301, '/');
    }
    const postureApi = createPostureApi({ token });
    try {
        const posture = await postureApi.getRandomPosture();
        return { posture };
    } catch (e) {
        console.error(e);
        return { posture: null };
    }
}