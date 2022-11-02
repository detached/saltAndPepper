import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import {BrowserRouter, Route, Routes} from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css"
import "./purecss-layout.css";

import "./i18n";

import RootRoute from "./routes/rootRoute";
import SearchRoute from "./routes/searchRoute";
import NewRecipeRoute from "./routes/newRecipeRoute";
import ProfileRoute from "./routes/profileRoute";
import LogoutRoute from "./routes/logoutRoute";
import RecipeRoute from "./routes/recipeRoute";
import EditRecipeRoute from "./routes/editRecipeRoute";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<RootRoute/>}>
          <Route index element={<SearchRoute/>} />
          <Route path="search" element={<SearchRoute/>} index />
          <Route path="recipe/new" element={<NewRecipeRoute/>} />
          <Route path="recipe/:recipeId/edit" element={<EditRecipeRoute/>} />
          <Route path="recipe/:recipeId" element={<RecipeRoute/>} />
          <Route path="profile" element={<ProfileRoute/>} />
          <Route path="logout" element={<LogoutRoute/>}/>
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
