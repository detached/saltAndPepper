import React from "react";
import ReactDOM from "react-dom/client";
import { HashRouter, Route, Routes } from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css";
import "./index.css";

import "./i18n.js";

import RootRoute from "./routes/rootRoute.jsx";
import NewsRoute from "./routes/newsRoute.jsx";
import SearchRoute from "./routes/searchRoute.jsx";
import NewRecipeRoute from "./routes/newRecipeRoute.jsx";
import ProfileRoute from "./routes/profileRoute.jsx";
import LogoutRoute from "./routes/logoutRoute.jsx";
import RecipeRoute from "./routes/recipeRoute.jsx";
import EditRecipeRoute from "./routes/editRecipeRoute.jsx";
import { AuthProvider } from "./context/authProvider.jsx";
import LoginRoute from "./routes/loginRoute.jsx";
import InviteRoute from "./routes/InviteRoute.jsx";

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
  </React.StrictMode>,
);
