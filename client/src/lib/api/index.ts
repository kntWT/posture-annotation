import { AnnotationApi, PostureApi, UserApi } from "./generated/apis";
import { Configuration } from "./generated/runtime";

type CreateConfigParam = {
    token?: string;
    basePath?: string;
}

const createConfig = (param: CreateConfigParam) => {
    return new Configuration({
        basePath: param.basePath ?? import.meta.env.VITE_API_SERVER_URL,
        accessToken: param.token ?? "",
    });
}
const config = createConfig({});
const userApi = new UserApi(config);
const postureApi = new PostureApi(config);
const createPostureApi = (param: CreateConfigParam) => new PostureApi(createConfig(param));
const createAnnotationApi = (param: CreateConfigParam) => new AnnotationApi(createConfig(param));

export { userApi, postureApi, createConfig, createPostureApi, createAnnotationApi };