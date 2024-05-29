import type { LayoutServerLoad } from './$types';
import { userApi } from '$api/userApi';

export const load: LayoutServerLoad = async ({ cookies }) => {
    const token = cookies.get('token');
    if (!token || token === '') {
        return {
            user: null
        }
    }
    const user = await userApi.getUserByToken(token)
        .then((response) => ({ ...response.data, token: "" }))
        .catch((e) => {
            cookies.set('token', '', { path: "/" })
            return null;
        });

    return user ? { user } : { user: null };
}
