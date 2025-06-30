import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { registerUser } from "../ts/api";

const Signup: React.FC = () => {
  const navigate = useNavigate();
  const [mid, setMid] = useState<string>("");
  const [mpw, setMpw] = useState<string>("");
  const [mpwCheck, setMpwCheck] = useState<string>("");
  const [email, setEmail] = useState<string>("");
  const [mname, setMname] = useState<string>("");
  const [errorMsg, setErrorMsg] = useState<string>("");
  const [successMsg, setSuccessMsg] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const handleSignup = async () => {
    if (isLoading) return;
    if (!mid.trim()) {
      setErrorMsg("아이디를 입력해주세요.");
      return;
    }
    if (!mpw.trim()) {
      setErrorMsg("비밀번호를 입력해주세요.");
      return;
    }
    if (!email.trim()) {
      setErrorMsg("이메일을 입력해주세요.");
      return;
    }
    if (!mname.trim()) {
      setErrorMsg("이름을 입력해주세요.");
      return;
    }
    if (mpw !== mpwCheck) {
      setErrorMsg("비밀번호가 일치하지 않습니다.");
      return;
    }
    setIsLoading(true);
    setErrorMsg("");
    setSuccessMsg("");
    try {
      await registerUser({ mid, mpw, email, mname });
      setSuccessMsg("회원가입이 완료되었습니다.");
      setTimeout(() => {
        navigate("/login");
      }, 1000);
    } catch (err: any) {
      let errorMessage = "회원가입 중 오류가 발생했습니다.";
      if (err.response?.data?.error) {
        errorMessage = err.response.data.error;
      } else if (err.response?.data?.message) {
        errorMessage = err.response.data.message;
      } else if (err.message?.includes("Network Error")) {
        errorMessage = "네트워크 연결을 확인해주세요.";
      } else if (err.request) {
        errorMessage =
          "서버에 연결할 수 없습니다. 서버가 실행 중인지 확인해주세요.";
      }
      setErrorMsg(errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="signup">
      <h1>회원가입</h1>
      <div>
        <input
          type="text"
          placeholder="Username"
          value={mid}
          onChange={(e) => setMid(e.target.value)}
          disabled={isLoading}
        />
        <input
          type="password"
          placeholder="Password"
          value={mpw}
          onChange={(e) => setMpw(e.target.value)}
          disabled={isLoading}
        />
        <input
          type="password"
          placeholder="Confirm Password"
          value={mpwCheck}
          onChange={(e) => setMpwCheck(e.target.value)}
          disabled={isLoading}
        />
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          disabled={isLoading}
        />
        <input
          type="text"
          placeholder="Name"
          value={mname}
          onChange={(e) => setMname(e.target.value)}
          disabled={isLoading}
        />
        <button
          onClick={handleSignup}
          disabled={isLoading}
          style={{
            backgroundColor: isLoading ? "#ccc" : "",
            cursor: isLoading ? "not-allowed" : "pointer",
          }}
        >
          {isLoading ? "가입 중..." : "가입하기"}
        </button>
      </div>
      {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
      {successMsg && <p style={{ color: "green" }}>{successMsg}</p>}
    </div>
  );
};

export default Signup;
