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

export interface ProductListDTO {
  pno: number;
  pname: string;
  price: number;
  writer: string;
  productImage: string; 
  reviewCount: number;
  join_date: string;
}

export interface ProductListPage {
  content: ProductListDTO[];
  totalPages: number;
  totalElements: number;
  number: number; // 현재 페이지 (0부터 시작)
  size: number;
}

export const getProductList = async (
  page: number = 1
): Promise<ProductListPage> => {
  const path = url + `products/list?page=${page}`;
  const res = await jwtAxios.get(path);
  return res.data;
};

export interface ProductDetailDTO {
  pno: number;
  pname: string;
  price: number;
  content: string;
  writer: string;
  imageList: string[];
  reviewCount: number;
}

// 상품 등록
export const registerProduct = async (
  product: Omit<ProductDetailDTO, "pno" | "reviewCount">
) => {
  const path = url + "products";
  const res = await jwtAxios.post(path, product);
  return res.data;
};

// 상품 상세
export const getProductDetail = async (
  pno: number
): Promise<ProductDetailDTO> => {
  const path = url + `products/${pno}`;
  const res = await jwtAxios.get(path);
  return res.data;
};

// 상품 수정
export const updateProduct = async (
  pno: number,
  product: Omit<ProductDetailDTO, "pno" | "reviewCount">
) => {
  const path = url + `products/${pno}`;
  const res = await jwtAxios.put(path, product);
  return res.data;
};

// 상품 삭제
export const deleteProduct = async (pno: number) => {
  const path = url + `products/${pno}`;
  const res = await jwtAxios.delete(path);
  return res.data;
};
