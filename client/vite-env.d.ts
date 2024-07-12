interface ImportMetaEnv {
    readonly VITE_API_SERVER_URL: string;
    readonly VITE_API_CLIENT_URL: string;
    readonly VITE_API_ENDPOINT: string;
    readonly VITE_FILE_SERVER_URL: string;
    readonly VITE_FILE_SERVER_ENDPOINT: string;
    readonly VITE_COOKIE_PREFIX: string;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}