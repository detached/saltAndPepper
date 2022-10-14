import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";

export default function RecipeList(props) {

    const {t} = useTranslation();
    const {page, listItems, switchToPageCallback} = props;
    const {navigateTo} = useNavigate();

    /**
     * @param recipe {SearchResponseData}
     */
    function navigateToRecipe(recipe) {
        navigateTo("/recipe/" + recipe.id);
    }

    /**
     * @param page {Page}
     * @returns {JSX.Element}
     */
    function pagination(page) {
        return <div className="pure-menu pure-menu-horizontal centered-text">
            <ul>
                {
                    page.number - 2 >= 0 ?
                        <li className="pure-menu-item">
                            <button className="pure-menu-link"
                                    onClick={() => switchToPageCallback(0)}>{t("pagination.first")}</button>
                        </li> : null
                }
                {
                    page.number - 1 >= 0 ?
                        <li className="pure-menu-item">
                            <button className="pure-menu-link"
                                    onClick={() => switchToPageCallback(page.number - 1)}>&#10218;</button>
                        </li> : null
                }
                <li className="pure-menu-item pure-menu-selected">
                    <button className="pure-menu-link">{page.number}</button>
                </li>
                {
                    page.number + 1 <= page.maxNumber ?
                        <li className="pure-menu-item">
                            <button className="pure-menu-link"
                                    onClick={() => switchToPageCallback(page.number + 1)}>&#10219;</button>
                        </li> : null
                }
                {
                    page.number + 2 <= page.maxNumber ?
                        <li className="pure-menu-item">
                            <button className="pure-menu-link"
                                    onClick={() => switchToPageCallback(page.maxNumber)}>{t("pagination.last")}</button>
                        </li> : null
                }
            </ul>
        </div>
    }

    /**
     * @param listItems {SearchResponseData[]}
     */
    function searchTable(listItems) {
        return <table id="recipe-table" className="pure-table pure-table-horizontal pure-table-striped">
            <thead>
            <tr>
                <th></th>
                <th>{t("search.table.title")}</th>
                <th>{t("search.table.category")}</th>
                <th>{t("search.table.cuisine")}</th>
                <th>{t("search.table.author")}</th>
            </tr>
            </thead>
            <tbody>
            {listItems.map(recipe =>
                <tr key={recipe.id} onClick={() => navigateToRecipe(recipe)}>
                    <td>{recipe.imageUrl ? <img src={recipe.imageUrl} alt=""/> : null}</td>
                    <td className="recipe-list-td">{recipe.title}</td>
                    <td>{recipe.category}</td>
                    <td>{recipe.cuisine}</td>
                    <td>{recipe.author}</td>
                </tr>)}
            </tbody>
        </table>
    }

    console.log(page);

    return <>
        {pagination(page)}
        {searchTable(listItems)}
        {pagination(page)}
    </>;
}
