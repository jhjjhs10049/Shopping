import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./jsx/login";
import List from "./jsx/list";
import Main from "./jsx/main";
import Signup from "./jsx/signup";
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
