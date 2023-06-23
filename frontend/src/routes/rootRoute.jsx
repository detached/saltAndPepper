import { NavLink, Outlet } from "react-router-dom";
import { createRef } from "react";
import { useTranslation } from "react-i18next";
import { ProfileProvider } from "../context/profileProvider";
import "./rootRoute.css";

export default function RootRoute() {
  const { t } = useTranslation();

  const menuLinkRef = createRef();
  const menuRef = createRef();
  const layoutRef = createRef();

  const toggleMenu = () => {
    const toggleActive = (element) => {
      const className = "active";
      if (element.classList.contains(className)) {
        element.classList.remove(className);
      } else {
        element.classList.add(className);
      }
    };

    toggleActive(layoutRef.current);
    toggleActive(menuRef.current);
    toggleActive(menuLinkRef.current);
  };

  const menuItem = (href, name) => (
    <li className="pure-menu-item">
      <NavLink
        to={href}
        className={({ isActive }) =>
          isActive ? "pure-menu-link pure-menu-selected" : "pure-menu-link"
        }
      >
        {name}
      </NavLink>
    </li>
  );

  return (
    <div id="layout" ref={layoutRef}>
      <div
        ref={menuLinkRef}
        id="menuLink"
        className="menu-link"
        onClick={toggleMenu}
      >
        <span></span>
      </div>

      <div id="menu" ref={menuRef} onClick={toggleMenu}>
        <div className="pure-menu">
          <ul className="pure-menu-list">
            {menuItem('news', t("menu.news"))}
            {menuItem("search", t("menu.search"))}
            {menuItem("recipe/new", t("menu.newRecipe"))}
            {menuItem("profile", t("menu.profile"))}
            {menuItem("logout", t("menu.logout"))}
          </ul>
        </div>
      </div>

      <div id="main">
        <ProfileProvider>
          <Outlet />
        </ProfileProvider>
      </div>
    </div>
  );
}
