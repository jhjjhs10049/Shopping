import React, { useState } from "react";
import Cookies from "universal-cookie";
import axios from "axios";
import { registerProduct } from "../../ts/api";

const ProductRegister: React.FC = () => {
  const [pname, setPname] = useState("");
  const [price, setPrice] = useState(0);
  const [content, setContent] = useState("");
  const [files, setFiles] = useState<File[]>([]);
  const [imageList, setImageList] = useState<string[]>([]);
  const [msg, setMsg] = useState("");
  const [uploading, setUploading] = useState(false);

  const cookies = new Cookies();
  const mid = cookies.get("mid"); // 로그인한 사용자 아이디

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files) {
      setFiles(Array.from(e.target.files));
    }
  };

  const handleUpload = async () => {
    if (files.length === 0) {
      setMsg("업로드할 파일을 선택하세요.");
      return;
    }
    setUploading(true);
    const formData = new FormData();
    files.forEach((file) => formData.append("files", file));
    try {
      const res = await axios.post(
        "http://localhost:8080/api/v1/files/upload",
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" },
        }
      );
      // 서버에서 저장된 파일명 배열을 반환 (res.data는 배열)
      setImageList(res.data || []);
      setMsg("이미지 업로드 성공");
    } catch (e: any) {
      setMsg(e?.response?.data?.error || "이미지 업로드 실패");
    } finally {
      setUploading(false);
    }
  };

  const handleRegister = async () => {
    if (!mid) {
      setMsg("로그인이 필요합니다.");
      return;
    }
    if (
      !pname.trim() ||
      !content.trim() ||
      imageList.length === 0 ||
      !imageList[0]
    ) {
      setMsg("모든 항목을 입력하세요. (이미지는 1개 이상 업로드 필요)");
      return;
    }
    try {
      await registerProduct({ pname, price, content, imageList, writer: mid });
      setMsg("상품이 등록되었습니다.");
      setTimeout(() => {
        window.location.href = "/list"; // 등록 후 목록으로 이동
      }, 1000);
    } catch (e: any) {
      setMsg(e?.response?.data?.error || "등록 실패");
    }
  };

  return (
    <div>
      <h2>상품 등록</h2>
      <table border={1} style={{ width: "100%", marginBottom: 16 }}>
        <tbody>
          <tr>
            <td>상품명</td>
            <td>
              <input value={pname} onChange={(e) => setPname(e.target.value)} />
            </td>
          </tr>
          <tr>
            <td>가격</td>
            <td>
              <input
                type="number"
                value={price}
                onChange={(e) => setPrice(Number(e.target.value))}
              />
            </td>
          </tr>
          <tr>
            <td>설명</td>
            <td>
              <input
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </td>
          </tr>
          <tr>
            <td>이미지 파일</td>
            <td>
              <input type="file" multiple onChange={handleFileChange} />
              <button
                type="button"
                onClick={handleUpload}
                disabled={uploading || files.length === 0}
              >
                {uploading ? "업로드 중..." : "이미지 업로드"}
              </button>
              {imageList.length > 0 && (
                <div style={{ marginTop: 8 }}>
                  업로드된 파일명: {imageList.join(", ")}
                </div>
              )}
            </td>
          </tr>
        </tbody>
      </table>
      <button onClick={handleRegister}>등록</button>
      {msg && <p>{msg}</p>}
    </div>
  );
};

export default ProductRegister;
