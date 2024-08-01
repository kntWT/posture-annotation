import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { createPostureApi } from "$api";


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
        }
        const posture = await postureApi.getRandomSamplePosture();
        return { posture };
    } catch (e) {
        console.error(e);
        return { posture: null };
    }
}