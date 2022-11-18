import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { HashRouter, Route, Routes } from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css";
import "./purecss-layout.css";

import "./i18n";

import RootRoute from "./routes/rootRoute";
import SearchRoute from "./routes/searchRoute";
import NewRecipeRoute from "./routes/newRecipeRoute";
import ProfileRoute from "./routes/profileRoute";
import LogoutRoute from "./routes/logoutRoute";
import RecipeRoute from "./routes/recipeRoute";
import EditRecipeRoute from "./routes/editRecipeRoute";
import { AuthProvider } from "./context/authProvider";
import LoginRoute from "./routes/loginRoute";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <HashRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<RootRoute />}>
            <Route index element={<SearchRoute />} />
            <Route path="search" element={<SearchRoute />} />
            <Route path="recipe/new" element={<NewRecipeRoute />} />
            <Route path="recipe/:recipeId/edit" element={<EditRecipeRoute />} />
            <Route path="recipe/:recipeId" element={<RecipeRoute />} />
            <Route path="profile" element={<ProfileRoute />} />
            <Route path="logout" element={<LogoutRoute />} />
          </Route>
          <Route path="/login" element={<LoginRoute />} />
        </Routes>
      </AuthProvider>
    </HashRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
