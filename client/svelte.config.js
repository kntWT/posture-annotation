import adapter from '@sveltejs/adapter-node';
import { vitePreprocess } from '@sveltejs/vite-plugin-svelte';
import { loadEnv } from 'vite';

/** @type {import('@sveltejs/kit').Config} */
const env = loadEnv('', process.cwd());
const config = {
	// Consult https://kit.svelte.dev/docs/integrations#preprocessors
	// for more information about preprocessors
	preprocess: vitePreprocess(),

	kit: {
		// adapter-auto only supports some environments, see https://kit.svelte.dev/docs/adapter-auto for a list.
		// If your environment is not supported, or you settled on a specific environment, switch out the adapter.
		// See https://kit.svelte.dev/docs/adapters for more information about adapters.
		adapter: adapter(),
		alias: {
			// $lib: './src/lib',
			$api: './src/lib/api',
			$stores: './src/stores',
		},
	},
    server: {
        proxy: {
            '/api': {
                target: env.VITE_API_SERVER_URL,
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/api/, ''),
            },
            '/images': {
                target: env.VITE_FILE_SERVER_URL,
                changeOrigin: true,
                rewrite: (path) => path.replace(/^\/images/, ''),
            },
        }
    }
};

export default config;
