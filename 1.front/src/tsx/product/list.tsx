import React, { useEffect, useState } from "react";
import { getProductList, ProductListPage } from "../../ts/api";

const ListPage: React.FC = () => {
  const [data, setData] = useState<ProductListPage | null>(null);
  const [error, setError] = useState<unknown>(null);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);

  useEffect(() => {
    const fetchData = async () => {
      setLoading(true);
      try {
        const response = await getProductList(page);
        setData(response);
        setError(null);
      } catch (err: any) {
        setError(err);
        setData(null);
        alert("ERROR");
        const errorMsg = err?.response?.data?.error;
        if (errorMsg && errorMsg.includes("REFRESH")) {
          window.location.href = "/login.html";
        }
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, [page]);

  const handlePageChange = (newPage: number) => {
    setPage(newPage);
  };

  return (
    <div>
      <h1>상품 목록</h1>
      <button
        style={{ marginBottom: 16 }}
        onClick={() => {
          window.location.href = "/product/register";
        }}
      >
        상품 등록
      </button>
      {loading && <p>로딩 중...</p>}
      {error ? <p>데이터를 불러오는 중 오류가 발생했습니다.</p> : null}
      {data && (
        <>
          <table border={1} style={{ width: "100%", marginBottom: 16 }}>
            <thead>
              <tr>
                <th>번호</th>
                <th>상품명</th>
                <th>가격</th>
                <th>작성자</th>
                <th>이미지</th>
                <th>리뷰수</th>
              </tr>
            </thead>
            <tbody>
              {data.content.map((item) => (
                <tr key={item.pno}>
                  <td>{item.pno}</td>
                  <td
                    style={{
                      cursor: "pointer",
                      color: "blue",
                      textDecoration: "underline",
                    }}
                    onClick={() => {
                      window.location.href = `/product/detail/${item.pno}`;
                    }}
                  >
                    {item.pname}
                  </td>
                  <td>{item.price.toLocaleString()}원</td>
                  <td>{item.writer}</td>
                  <td>
                    {" "}
                    {Array.isArray(item.imageList) &&
                    item.imageList.length > 0 ? (
                      item.imageList.map((img: string, idx: number) =>
                        img && img !== "null" && img !== "" ? (
                          <img
                            key={idx}
                            src={`http://localhost:8080/upload/${encodeURIComponent(
                              img
                            )}`}
                            alt=""
                            width={60}
                            style={{ marginRight: 4 }}
                            onError={(e) =>
                              (e.currentTarget.style.display = "none")
                            }
                          />
                        ) : null
                      )
                    ) : item.productImage &&
                      item.productImage !== "null" &&
                      item.productImage !== "" ? (
                      <img
                        src={`http://localhost:8080/upload/${encodeURIComponent(
                          item.productImage
                        )}`}
                        alt=""
                        width={300}
                        onError={(e) =>
                          (e.currentTarget.style.display = "none")
                        }
                      />
                    ) : (
                      "-"
                    )}
                  </td>
                  <td>
                    {typeof item.reviewCount === "number" &&
                    !isNaN(item.reviewCount)
                      ? item.reviewCount
                      : 0}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          <div style={{ display: "flex", gap: 8 }}>
            <button
              onClick={() => handlePageChange(page - 1)}
              disabled={page <= 1}
            >
              이전
            </button>
            <span>
              {page} / {data.totalPages}
            </span>
            <button
              onClick={() => handlePageChange(page + 1)}
              disabled={page >= data.totalPages}
            >
              다음
            </button>
          </div>
        </>
      )}
    </div>
  );
};

export default ListPage;
