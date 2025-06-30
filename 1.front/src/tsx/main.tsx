import React from "react";
import { Link } from "react-router-dom";

const Main: React.FC = () => {
  return (
    <div>
      <h1>🛍️ 쇼핑몰에 오신 것을 환영합니다!</h1>
      <p>최고의 상품을 지금 만나보세요.</p>
      <Link to="/signup">
        <button>회원가입</button>
      </Link>
      <Link to="/login">
        <button>로그인</button>
      </Link>
    </div>
  );
};

export default Main;