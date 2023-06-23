import React from "react";
import ReactDOM from "react-dom/client";
import { HashRouter, Route, Routes } from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css";
import "./index.css";

import "./i18n";

import RootRoute from "./routes/rootRoute";
import NewsRoute from "./routes/newsRoute";
import SearchRoute from "./routes/searchRoute";
import NewRecipeRoute from "./routes/newRecipeRoute";
import ProfileRoute from "./routes/profileRoute";
import LogoutRoute from "./routes/logoutRoute";
import RecipeRoute from "./routes/recipeRoute";
import EditRecipeRoute from "./routes/editRecipeRoute";
import { AuthProvider } from "./context/authProvider";
import LoginRoute from "./routes/loginRoute";
import InviteRoute from "./routes/InviteRoute";

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <HashRouter>
      <AuthProvider>
        <Routes>
          <Route path="/" element={<RootRoute />}>
            <Route index element={<NewsRoute />} />
            <Route path="news" element={<NewsRoute />} />
            <Route path="search" element={<SearchRoute />} />
            <Route path="recipe/new" element={<NewRecipeRoute />} />
            <Route path="recipe/:recipeId/edit" element={<EditRecipeRoute />} />
            <Route path="recipe/:recipeId" element={<RecipeRoute />} />
            <Route path="profile" element={<ProfileRoute />} />
            <Route path="logout" element={<LogoutRoute />} />
          </Route>
          <Route path="/login" element={<LoginRoute />} />
          <Route path="/invite/:code" element={<InviteRoute />} />
        </Routes>
      </AuthProvider>
    </HashRouter>
  </React.StrictMode>
);
