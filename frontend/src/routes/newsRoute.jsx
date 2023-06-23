import "./newsRoute.css";
import { useTranslation } from "react-i18next";
//import NewAuthors from "../components/newAuthors";
import NewRecipes from "../components/newRecipes";

export default function NewsRoute() {

    const { t } = useTranslation();

    return (
        <div className="pure-g">

            <div className="pure-u-1"> {/*pure-u-md-4-5*/}
                <div className="header">
                    <h2>{t("news.recipes")}</h2>
                </div>
                <NewRecipes />
            </div>

            {/*<div className="pure-u-1 pure-u-md-1-5">
                <div className="header">
                    <h2>{t("news.authors")}</h2>
                </div>
                <NewAuthors />
            </div>*/}            
        </div>
    );
}