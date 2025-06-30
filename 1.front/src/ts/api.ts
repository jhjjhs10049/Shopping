import axios from "axios";
import Cookies from "universal-cookie";
import jwtAxios from "./customAxios";

const cookies = new Cookies(null, { path: "/", maxAge: 2592000 });
const url = "http://localhost:8080/api/v1/";

export const testApi = (): void => {
  console.log("test Api...");
};

export async function requestRefreshToken(): Promise<any> {
  const refreshToken = cookies.get("refreshToken");
  const mid = cookies.get("mid");
  const accessToken = cookies.get("accessToken");
  if (!mid || !refreshToken || !accessToken) {
    throw Error("Cannot request refresh..");
  }
  const path = url + "token/refresh";
  const header = {
    "content-type": "application/x-www-form-urlencoded",
    Authorization: "Bearer " + accessToken,
  };
  const data = { refreshToken, mid };
  const res = await axios.post(path, data, { headers: header });
  return res.data;
}

export const saveToken = (tokenName: string, tokenValue: string): void => {
  cookies.set(tokenName, tokenValue);
};

export const makeToken = async (mid: string, mpw: string): Promise<any> => {
  const path = url + "token/make";
  const data = { mid, mpw };
  const res = await axios.post(path, data);
  return res.data;
};

export const registerUser = async (memberData: {
  mid: string;
  mpw: string;
  email: string;
  mname: string;
}): Promise<any> => {
  const path = url + "member/register";
  try {
    const res = await axios.post(path, memberData);
    return res.data;
  } catch (error: any) {
    throw error;
  }
};

export const getSamples = async (pageNum: number): Promise<any> => {
  const path = url + "samples/list";
  const res = await jwtAxios.get(path);
  return res.data;
};
