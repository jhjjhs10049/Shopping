import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./tsx/login";
import List from "./tsx/list";
import Main from "./tsx/main";
import Signup from "./tsx/signup";
import "./css/index.css";
import React from "react";

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/login" element={<Login />} />
        <Route path="/list" element={<List />} />
      </Routes>
    </Router>
  );
};

export default App;
