import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import reportWebVitals from "./reportWebVitals";
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import "purecss/build/pure-min.css";
import "purecss/build/grids-responsive-min.css"
import "./purecss-layout.css";

import "./i18n";

import Root from "./routes/root";
import Search from "./routes/search";
import NewRecipe from "./routes/newRecipe";
import Profile from "./routes/profile";
import Logout from "./routes/logout";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        path: "search",
        element: <Search />,
      },
      {
        path: "recipe/new",
        element: <NewRecipe />,
      },
      {
        path: "profile",
        element: <Profile />,
      },
      {
        path: "logout",
        element: <Logout />,
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
