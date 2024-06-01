import { PostureApi, UserApi } from "./generated/apis";
import { Configuration, type ErrorContext, type Middleware } from "./generated/runtime";

const config = new Configuration();
const userApi = new UserApi(config);
const postureApi = new PostureApi(config);

export { userApi, postureApi };