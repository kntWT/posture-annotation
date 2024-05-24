import { fail, redirect, type Cookies } from '@sveltejs/kit';
import type { PageServerLoad, Actions } from './$types';
import type { User, UserCreate } from '$api/generated';
import { userApi } from '$api/userApi';

/** @type {import('./$types').PageServerLoad} */
export const load: PageServerLoad = async ({ cookies }) => {
    const token = cookies.get('token');
    if (token || token !== '') {
        // redirect(308, '/');
    }
}

const parseAndValidateForm = async (request: Request): Promise<UserCreate | null> => {
    const formData = await request.formData();
    const name = formData.get('name') as string;
    const password = formData.get('password') as string;
    if (!name || !password || name === "" || password === "") {
        return null;
    }
    return { name, password };
}

/** @type {import('./$types').Actions} */
export const actions: Actions = {
    login: async ({ request, cookies }) => {
        const userInfo = await parseAndValidateForm(request);
        if (userInfo === null) {
            return fail(400, { action: "ログイン", message: "ユーザ名とパスワードを入力してください", missing: true });
        }

        const user: User | null = await userApi.loginUser(userInfo)
            .then((response) => response.data)
            .catch((e) => null);
        
        if (user === null) {
            return fail(401, { action: "ログイン", message: "ユーザ名またはパスワードが違います", incorrect: true });
        } else {
            cookies.set("token", user.token, { path: "/" });
            return { status: 200, data: { user } };
        }
    },

    signup: async ({ request, cookies }) => {
        const userInfo = await parseAndValidateForm(request);
        if (userInfo === null) {
            return fail(400, { action: "新規登録", message: "ユーザ名とパスワードを入力してください", missing: true });
        }

        const user: User | null = await userApi.createUser(userInfo)
            .then((response) => response.data)
            .catch((e) => null);
        
        if (user === null) {
            return fail(400, { action: "新規登録", message: "無効なユーザ名またはパスワードです", incorrect: true });
        } else {
            cookies.set("token", user.token, { path: "/" });
            return { status: 200, data: { user } };
        }
    }
}