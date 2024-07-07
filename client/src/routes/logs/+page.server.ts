import { redirect } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { createAnnotationApi, userApi } from "$api";
import { toBearer } from '../../lib/util';



export const load: PageServerLoad = async ({ cookies }) => {
    const token = cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`);
    if (!token || token === '' || token === 'undefined') {
        redirect(301, '/');
    }

    const annotationApi = createAnnotationApi({ token });
    try {
        const user = await userApi.getUserByToken({ authorization: toBearer(token) });
        const annotations = await annotationApi.getAnnotationsWithFilePathByAnnotaterId(
            { annotaterId: user.id }
        );
        return { annotations };
    } catch (e) {
        console.error(e);
        return { annotations: [] };
    }
}