import { sveltekit } from '@sveltejs/kit/vite';
import { defineConfig } from 'vitest/config';
import { loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
	const env = loadEnv(mode, process.cwd());
	return {
		plugins: [sveltekit()],
		test: {
			include: ['src/**/*.{test,spec}.{js,ts}']
		},
		server: {
			host: true,
			proxy: {
				'/api/': {
					target: env.VITE_API_SERVER_URL,
					changeOrigin: true,
					rewrite: path => path.replace(/^\/api\//, '/')
				},
				'/images/': {
					target: env.VITE_FILE_SERVER_URL,
					changeOrigin: true,
					rewrite: path => path.replace(/^\/images\//, '/')
				},
			}
		}
	}
});
