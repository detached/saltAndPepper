import {useTranslation} from "react-i18next";
import "./newRecipeRoute.css"
import {useCallback, useState} from "react";
import {NewRecipeRequest, SaltAndPepper} from "../api/saltAndPepper";
import {useNavigate} from "react-router-dom";

export default function NewRecipeRoute() {

    const {t} = useTranslation();
    const navigateTo = useNavigate();

    const [isLoading, setIsLoading] = useState(false);
    const [title, setTitle] = useState("");
    const [category, setCategory] = useState("");
    const [cuisine, setCuisine] = useState("");
    const [yields, setYields] = useState("");
    const [ingredients, setIngredients] = useState("");
    const [instructions, setInstructions] = useState("");
    const [modifications, setModifications] = useState("");

    const createNewRecipe = useCallback((e) => {
        e.preventDefault();
        if (isLoading) {
            return;
        }
        setIsLoading(true);

        const request = new NewRecipeRequest(title, category, cuisine, yields, ingredients, instructions, modifications);
        SaltAndPepper.newRecipe(request).then((result) => {
            navigateTo("/recipe/" + result.id);
        }).finally(() => {
            setIsLoading(false)
        })
    }, [title, category, cuisine, yields, ingredients, instructions, modifications, isLoading, navigateTo])

    return <>
        <div className="header">
            <h1>{t("newRecipe.title")}</h1>
        </div>
        <div className="content pure-g">
            <form className="pure-form pure-form-stacked">
                <div className="pure-u-1 pure-u-md-1-2">
                    <img className="pure-img-responsive" src="https://picsum.photos/300" alt=""/>
                </div>
                <div className="pure-u-1 pure-u-md-1-2">
                    <fieldset>
                        <label htmlFor="title">{t("newRecipe.name")}:</label>
                        <input type="text" id="title" name="title" placeholder={t("newRecipe.name")}
                               className="pure-input-1" value={title} onChange={ e => setTitle(e.target.value)} />
                        <label htmlFor="category">{t("recipe.category")}:</label>
                        <input type="text" id="category" name="category"
                               placeholder={t("recipe.category")} className="pure-input-1" value={category}
                               onChange={e => setCategory(e.target.value)}/>
                        <label htmlFor="cuisine">{t("recipe.cuisine")}:</label>
                        <input type="text" id="cuisine" name="cuisine"
                               placeholder={t("recipe.cuisine")} className="pure-input-1" value={cuisine}
                               onChange={e => setCuisine(e.target.value)}/>
                        <label htmlFor="yields">{t("recipe.yields")}:</label>
                        <input type="text" id="yields" name="yields" placeholder={t("recipe.yields")}
                               className="pure-input-1" value={yields} onChange={e => setYields(e.target.value)}/>
                    </fieldset>
                </div>
                <div className="pure-u-1">
                    <h2>{t("recipe.ingredients")}</h2>
                    <textarea id="ingredients" name="ingredients" className="newrecipe-textarea newrecipe-texterea-big"
                              placeholder={t('recipe.ingredients')}
                              value={ingredients} onChange={e => setIngredients(e.target.value)}></textarea>

                    <h2>{t("recipe.instructions")}</h2>
                    <textarea id="instructions" name="instructions"
                              className="newrecipe-textarea newrecipe-texterea-big"
                              placeholder={t('recipe.instructions')}
                              value={instructions} onChange={e => setInstructions(e.target.value)}></textarea>

                    <h2>{t("recipe.modifications")}</h2>
                    <textarea id="modifications" name="modifications" className="newrecipe-textarea"
                              placeholder={t('recipe.modifications')}
                              value={modifications} onChange={e => setModifications(e.target.value)}></textarea>

                    <button type="submit" className="pure-button pure-button-primary newrecipe-submit"
                            disabled={isLoading}
                            onClick={ e => createNewRecipe(e)}>
                        {t("newRecipe.submit")}
                    </button>
                </div>
            </form>
        </div>
    </>;
}
