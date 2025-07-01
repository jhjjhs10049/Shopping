import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { getProductDetail, deleteProduct } from "../../ts/api";

const ProductDetail: React.FC = () => {
  const { pno } = useParams<{ pno: string }>();
  const [data, setData] = useState<any>(null);
  const [msg, setMsg] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    if (!pno) return;
    getProductDetail(Number(pno))
      .then(setData)
      .catch(() => setMsg("상세 조회 실패"));
  }, [pno]);

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
      <h2>상품 상세</h2>
      <table border={1} style={{ width: "100%", marginBottom: 16 }}>
        <tbody>
          <tr>
            <td>번호</td>
            <td>{data.pno}</td>
          </tr>
          <tr>
            <td>상품명</td>
            <td>{data.pname}</td>
          </tr>
          <tr>
            <td>가격</td>
            <td>{data.price.toLocaleString()}원</td>
          </tr>
          <tr>
            <td>설명</td>
            <td>{data.content}</td>
          </tr>
          <tr>
            <td>작성자</td>
            <td>{data.writer}</td>
          </tr>
          <tr>
            <td>이미지</td>
            <td>
              {" "}
              {Array.isArray(data.imageList) && data.imageList.length > 0
                ? data.imageList.map((img: string, idx: number) =>
                    img && img !== "null" && img !== "" ? (
                      <img
                        key={idx}
                        src={`http://localhost:8080/upload/${encodeURIComponent(
                          img
                        )}`}
                        alt=""
                        width={600}
                        style={{ marginRight: 4 }}
                        onError={(e) =>
                          (e.currentTarget.style.display = "none")
                        }
                      />
                    ) : null
                  )
                : "-"}
            </td>
          </tr>
        </tbody>
      </table>
      <button onClick={() => navigate(`/product/edit/${data.pno}`)}>
        수정
      </button>
      <button onClick={handleDelete} style={{ marginLeft: 8 }}>
        삭제
      </button>
      {msg && <p>{msg}</p>}
    </div>
  );
};

export default ProductDetail;
