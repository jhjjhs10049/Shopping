import axios, { InternalAxiosRequestConfig, AxiosResponse } from "axios";
import Cookies from "universal-cookie";
import { requestRefreshToken, saveToken } from "./api";

const jwtAxios = axios.create();
const cookies = new Cookies(null, { path: "/", maxAge: 2592000 });

const beforeRequest = (
  config: InternalAxiosRequestConfig
): InternalAxiosRequestConfig => {
  const noTokenUrls = ["/member/register", "/token/make", "/token/refresh"];
  const isNoTokenUrl = noTokenUrls.some(
    (url) => config.url && config.url.includes(url)
  );
  if (isNoTokenUrl) return config;
  const accessToken = cookies.get("accessToken");
  if (!accessToken) throw Error("No Token");
  config.headers = config.headers || {};
  config.headers["Authorization"] = "Bearer " + accessToken;
  return config;
};

const beforeResponse = (response: AxiosResponse): AxiosResponse => {
  return response;
};

const errorResponse = async (error: any) => {
  if (error.response && error.response.status) {
    const status = error.response.status;
    const res = error.response.data;
    const errorMsg = res.error;
    const refreshFn = async () => {
      const data = await requestRefreshToken();
      saveToken("accessToken", data.accessToken);
      saveToken("refreshToken", data.refreshToken);
      error.config.headers["Authorization"] = "Bearer " + data.accessToken;
      return await axios(error.config);
    };
    if (errorMsg && errorMsg.indexOf("expired") > 0) {
      return refreshFn();
    } else {
      return Promise.reject(error);
    }
  } else {
    return Promise.reject(error);
  }
};

jwtAxios.interceptors.request.use(beforeRequest);
jwtAxios.interceptors.response.use(beforeResponse, errorResponse);

export default jwtAxios;
