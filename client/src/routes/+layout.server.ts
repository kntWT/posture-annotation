import type { LayoutServerLoad } from './$types';
import { postureApi, userApi } from '$api';

export const load: LayoutServerLoad = async ({ cookies }) => {
    const token = cookies.get('token');
    if (!token || token === '') {
        return {
            user: null
        }
    }

    try {
        const user = await userApi.getUserByToken({ authorization: `Bearer ${token}` });
        return { user };
    } catch (e) {
        return { user: null };
    }
}
