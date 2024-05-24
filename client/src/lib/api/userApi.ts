import { UserApi } from "./generated";
import axios from "axios";

const axiosInstance = axios.create({});

export const userApi = new UserApi(undefined, undefined, axiosInstance);