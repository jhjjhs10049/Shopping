import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { makeToken, saveToken } from "../ts/api";

const Login: React.FC = () => {
  const navigate = useNavigate();
  const [mid, setMid] = useState<string>("");
  const [mpw, setMpw] = useState<string>("");
  const [errorMsg, setErrorMsg] = useState<string>("");

  const handleLogin = async () => {
    try {
      const data = await makeToken(mid, mpw);
      const { accessToken, refreshToken } = data;
      saveToken("accessToken", accessToken);
      saveToken("refreshToken", refreshToken);
      saveToken("mid", mid);
      navigate("/list");
    } catch (err: any) {
      console.log(err);
      if (err.response?.data?.type === "bad_credentials") {
        setErrorMsg(err.response.data.error);
      } else if (err.response?.data?.error) {
        setErrorMsg(err.response.data.error);
      } else {
        setErrorMsg("An error occurred");
      }
    }
  };

  return (
    <div className="login">
      <h1>Login</h1>
      <div>
        <input
          type="text"
          placeholder="Username"
          name="mid"
          value={mid}
          onChange={(e) => setMid(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          name="mpw"
          value={mpw}
          onChange={(e) => setMpw(e.target.value)}
        />
        <button className="loginBtn" onClick={handleLogin}>
          Login
        </button>
      </div>
      {errorMsg && <p style={{ color: "red" }}>{errorMsg}</p>}
    </div>
  );
};

export default Login;
