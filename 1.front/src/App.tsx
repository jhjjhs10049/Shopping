import { BrowserRouter as Router, Routes, Route } from "react-router-dom";

import Main from "./tsx/main";
//member
import Login from "./tsx/member/login";
import Signup from "./tsx/member/signup";
//product
import List from "./tsx/product/list";
import Register from "./tsx/product/register";
import Edit from "./tsx/product/edit";
import Detail from "./tsx/product/detail";



import "./css/index.css";
import React from "react";

const App: React.FC = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Main />} />
        //member
        <Route path="/login" element={<Login />} />
        <Route path="/signup" element={<Signup />} />
        //product
        <Route path="/list" element={<List />} />
        <Route path="/product/register" element={<Register />} />
        <Route path="/product/edit/:pno" element={<Edit />} />
        <Route path="/product/detail/:pno" element={<Detail />} />
      </Routes>
    </Router>
  );
};

export default App;
