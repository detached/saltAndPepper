import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css"
import "./purecss-layout.css";

import "./i18n";

import RootRoute from "./routes/rootRoute";
import SearchRoute from "./routes/searchRoute";
import NewRecipeRoute from "./routes/newRecipeRoute";
import ProfileRoute from "./routes/profileRoute";
import LogoutRoute from "./routes/logoutRoute";

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootRoute />,
    children: [
      {
        path: "search",
        element: <SearchRoute />,
      },
      {
        path: "recipe/new",
        element: <NewRecipeRoute />,
      },
      {
        path: "profile",
        element: <ProfileRoute />,
      },
      {
        path: "logout",
        element: <LogoutRoute />,
      },
    ],
  },
]);

ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
