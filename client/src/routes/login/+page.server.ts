import { redirect } from '@sveltejs/kit';
import type { PageServerLoad } from './$types';

/** @type {import('./$types').PageServerLoad} */
export const load: PageServerLoad = async ({ cookies }) => {
    const token = cookies.get('token');
    if (token || token !== '') {
        redirect(308, '/');
    }
}
