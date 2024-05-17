import type { LayoutServerLoad } from './$types';
import { userApi } from '$api/userApi';

export const load: LayoutServerLoad = async ({ cookies }) => {
    const token = cookies.get('token');
    if (!token || token === '') {
        return {
            user: null
        }
    }
    const user = await (userApi.getUserByToken({ data: { token } }))
        .then((response) => response.data)
        .catch((e) => {
            cookies.set('token', '', { path: "/" })
        });
    return user ? { user } : { user: null };
}
