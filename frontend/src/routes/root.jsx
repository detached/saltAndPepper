import { NavLink, Outlet } from "react-router-dom";
import React from "react";
import { useTranslation } from "react-i18next";

export default function Root() {
  const { t } = useTranslation();

  const menuLinkRef = React.createRef();
  const menuRef = React.createRef();
  const layoutRef = React.createRef();

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
      <a
        href="#menu"
        ref={menuLinkRef}
        id="menuLink"
        className="menu-link"
        onClick={toggleMenu}
      >
        <span></span>
      </a>

      <div id="menu" ref={menuRef} onClick={toggleMenu}>
        <div className="pure-menu">
          <ul className="pure-menu-list">
            {menuItem("search", t("menu.search"))}
            {menuItem("recipe/new", t("menu.newRecipe"))}
            {menuItem("import", t("menu.import"))}
            {menuItem("profile", t("menu.profile"))}
            {menuItem("logout", t("menu.logout"))}
          </ul>
        </div>
      </div>

      <div id="main">
        <Outlet />
      </div>
    </div>
  );
}
