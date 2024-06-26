import "./newsRoute.css";
import { useTranslation } from "react-i18next";
import NewRecipes from "../components/newRecipes.jsx";

export default function NewsRoute() {
  const { t } = useTranslation();

  return (
    <div className="pure-g">
      <div className="pure-u-1">
        {/*pure-u-md-4-5*/}
        <div className="header">
          <h2>{t("news.recipes")}</h2>
        </div>
        <NewRecipes />
      </div>
    </div>
  );
}
