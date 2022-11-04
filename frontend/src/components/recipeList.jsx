import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import "./recipeList.css";

export default function RecipeList({ page, listItems, setPage }) {
  const { t } = useTranslation();
  const navigateTo = useNavigate();

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
    return (
      <div className="pure-button-group recipe-list-pagination" role="group">
        <ul>
          {page.number - 2 >= 0 ? (
            <button
              name="first"
              className="pure-button"
              onClick={() => setPage(0)}
            >
              {t("pagination.first")}
            </button>
          ) : null}
          {page.number - 1 >= 0 ? (
            <button
              name="prev"
              className="pure-button"
              onClick={() => setPage(page.number - 1)}
            >
              &#10218;
            </button>
          ) : null}
          <button name="current" className="pure-button pure-button-active">
            {page.number}
          </button>
          {page.number + 1 <= page.maxNumber ? (
            <button
              name="next"
              className="pure-button"
              onClick={() => setPage(page.number + 1)}
            >
              &#10219;
            </button>
          ) : null}
          {page.number + 2 <= page.maxNumber ? (
            <button
              name="last"
              className="pure-button"
              onClick={() => setPage(page.maxNumber)}
            >
              {t("pagination.last")}
            </button>
          ) : null}
        </ul>
      </div>
    );
  }

  /**
   * @param listItems {SearchResponseData[]}
   */
  function searchTable(listItems) {
    return (
      <div className="recipe-list">
        <table className="pure-table pure-table-horizontal pure-table-striped">
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
            {listItems.map((recipe) => (
              <tr key={recipe.id} onClick={() => navigateToRecipe(recipe)}>
                <td>
                  {recipe.imageUrl ? (
                    <img src={recipe.imageUrl} alt="" />
                  ) : null}
                </td>
                <td>{recipe.title}</td>
                <td className="desktop-only">{recipe.category}</td>
                <td className="desktop-only">{recipe.cuisine}</td>
                <td className="desktop-only">{recipe.author}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    );
  }

  return (
    <>
      {pagination(page)}
      {searchTable(listItems)}
      {pagination(page)}
    </>
  );
}
