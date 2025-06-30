import React, { useEffect, useState } from "react";
import { getSamples } from "../js/api"; // getSamples의 타입이 명확하면 별도 타입 지정 추천

type SampleData = any; // 실제 API 응답 타입에 맞게 교체하는 것이 좋습니다 (예: Sample[] 등)

const ListPage: React.FC = () => {
  const [data, setData] = useState<SampleData | null>(null);
  const [error, setError] = useState<unknown>(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await getSamples(1); // 예시로 1을 전달, 필요에 따라 적절한 pageNum으로 변경
        setData(response);
      } catch (err: any) {
        console.error(err);
        setError(err);
        alert("ERROR");

        // 에러 메시지에 "REFRESH" 포함 시 로그인 페이지로 이동
        const errorMsg = err?.response?.data?.error;
        if (errorMsg && errorMsg.includes("REFRESH")) {
          window.location.href = "/login.html";
        }
      }
    };

    fetchData();
  }, []);

  return (
    <div>
      <h1>List Page</h1>
      {error ? (
        <p>Error occurred while fetching data!</p>
      ) : (
        <pre>{JSON.stringify(data, null, 2)}</pre>
      )}
    </div>
  );
};

export default ListPage;
