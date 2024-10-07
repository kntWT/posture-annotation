import { userApi } from '$api';
import { toBearer } from '$lib/util';
import { redirect, type Handle } from '@sveltejs/kit';

export const handle: Handle = async ({ event, resolve }) => {
	const pathname = event.url.pathname;
	const shouldLoginPaths = ['/logs', '/annotate', '/admin'];
	const shouldAdminPaths = ['/admin'];

	const token = event.cookies.get(`${import.meta.env.VITE_COOKIE_PREFIX}token`) || '';
	const user =
		token !== '' ? await userApi.getUserByToken({ authorization: toBearer(token) }) : null;
	// ログインしてなくてログインが必要ない場合はそのまま処理を続ける
	if (![...shouldLoginPaths, ...shouldAdminPaths].some((p) => pathname.startsWith(p))) {
		event.locals.user = user;
		return await resolve(event);
	}

	// ログインしてないのにログインが必要なページにアクセスしようとした場合はログインページにリダイレクト
	if (shouldLoginPaths.some((p) => pathname.startsWith(p)) && !user) {
		throw redirect(302, `${import.meta.env.VITE_BASE_PATH}/login`);
	}

	const userId = user?.id || 0;
	const adminUserIds: number[] = import.meta.env.VITE_ADMIN_USER_IDS.split(',').map(Number);
	const isAdmin = adminUserIds.includes(userId);
	// 管理者でないのに管理者ページにアクセスしようとした場合はトップページにリダイレクト
	if (shouldAdminPaths.some((p) => pathname.startsWith(p)) && !isAdmin) {
		throw redirect(302, `${import.meta.env.VITE_BASE_PATH}/`);
	}
	event.locals.user = user;

	const response = await resolve(event);
	return response;
};
