import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProductDetail, updateProduct, deleteProduct } from "../../ts/api";
import Cookies from "universal-cookie";

const ProductEdit: React.FC = () => {
  const { pno } = useParams<{ pno: string }>();
  const [data, setData] = useState<any>(null);
  const [msg, setMsg] = useState("");
  const [files, setFiles] = useState<FileList | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!pno) return;
    getProductDetail(Number(pno))
      .then(setData)
      .catch(() => setMsg("상세 조회 실패"));
  }, [pno]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setData((prev: any) => ({ ...prev, [name]: value }));
  };

  const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFiles(e.target.files);
  };

  const handleUpdate = async () => {
    if (!pno) return;
    try {
      let imageList = data.imageList || [];

      // 새 파일이 선택된 경우 업로드
      if (files && files.length > 0) {
        const formData = new FormData();
        Array.from(files).forEach((file) => {
          formData.append("files", file);
        });

        const response = await fetch(
          "http://localhost:8080/api/v1/files/upload",
          {
            method: "POST",
            body: formData,
          }
        );

        if (!response.ok) {
          throw new Error("파일 업로드 실패");
        }

        imageList = await response.json();
      }

      await updateProduct(Number(pno), {
        pname: data.pname,
        price: Number(data.price),
        content: data.content,
        writer: data.writer,
        imageList: imageList,
      });
      setMsg("수정 완료");
      setTimeout(() => navigate(`/product/detail/${pno}`), 1000);
    } catch {
      setMsg("수정 실패");
    }
  };

  const handleDelete = async () => {
    if (!pno) return;
    if (!window.confirm("정말 삭제하시겠습니까?")) return;
    try {
      await deleteProduct(Number(pno));
      setMsg("삭제 완료");
      setTimeout(() => navigate("/list"), 1000);
    } catch {
      setMsg("삭제 실패");
    }
  };

  if (!data) return <div>로딩 중...</div>;

  return (
    <div>
      <h2>상품 수정</h2>
      <table border={1} style={{ width: "100%", marginBottom: 16 }}>
        <tbody>
          <tr>
            <td>상품명</td>
            <td>
              <input name="pname" value={data.pname} onChange={handleChange} />
            </td>
          </tr>
          <tr>
            <td>가격</td>
            <td>
              <input
                name="price"
                type="number"
                value={data.price}
                onChange={handleChange}
              />
            </td>
          </tr>
          <tr>
            <td>설명</td>
            <td>
              <input
                name="content"
                value={data.content}
                onChange={handleChange}
              />
            </td>
          </tr>{" "}
          <tr>
            <td>이미지 업로드</td>
            <td>
              <input
                type="file"
                multiple
                accept="image/*"
                onChange={handleFileChange}
              />
              {data.imageList && data.imageList.length > 0 && (
                <div style={{ marginTop: 8 }}>
                  <p>현재 이미지:</p>
                  {data.imageList.map((img: string, idx: number) => (
                    <img
                      key={idx}
                      src={`http://localhost:8080/upload/${encodeURIComponent(
                        img
                      )}`}
                      alt=""
                      width={600}
                      style={{ marginRight: 4 }}
                      onError={(e) => (e.currentTarget.style.display = "none")}
                    />
                  ))}
                </div>
              )}
            </td>
          </tr>
        </tbody>
      </table>
      <button onClick={handleUpdate}>수정</button>
      <button onClick={handleDelete} style={{ marginLeft: 8 }}>
        삭제
      </button>
      {msg && <p>{msg}</p>}
    </div>
  );
};

export default ProductEdit;
