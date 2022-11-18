import { useNavigate, useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ImageGallery from "../components/imageGallery";
import { SaltAndPepper } from "../api/saltAndPepper";
import { useProfile } from "../context/profileProvider";

export default function RecipeRoute() {
  const { t } = useTranslation();
  const navigateTo = useNavigate();
  const { recipeId } = useParams();
  const [recipe, setRecipe] = useState(null);
  const profile = useProfile();

  useEffect(() => {
    SaltAndPepper.getRecipe(recipeId).then((result) => {
      setRecipe(result);
    });
  }, [recipeId, setRecipe]);

  function editRecipe() {
    navigateTo("/recipe/" + recipeId + "/edit");
  }

  function splitText(multilineText) {
    return multilineText.split("\n").map((line) => {
      return (
        <>
          {line}
          <br />
        </>
      );
    });
  }

  function allowedToEdit() {
    return profile.id === recipe.author.id;
  }

  if (recipe === null) {
    return null;
  } else {
    return (
      <>
        <div className="header">
          <h1>{recipe.title}</h1>
        </div>
        <div className="content centered">
          <ImageGallery images={recipe.images} />

          <div className="pure-g recipe-info">
            <div className="pure-u-1-2">{t("recipe.author")}</div>
            <div className="pure-u-1-2">{recipe.author.name}</div>
            <div className="pure-u-1-2">{t("recipe.category")}</div>
            <div className="pure-u-1-2">{recipe.category}</div>
            <div className="pure-u-1-2">{t("recipe.cuisine")}</div>
            <div className="pure-u-1-2">{recipe.cuisine}</div>
            <div className="pure-u-1-2">{t("recipe.yields")}</div>
            <div className="pure-u-1-2">{recipe.yields}</div>
          </div>
          <h2>{t("recipe.ingredients")}</h2>
          <p>{splitText(recipe.ingredients)}</p>

          <h2>{t("recipe.instructions")}</h2>
          <p>{splitText(recipe.instructions)}</p>

          <h2>{t("recipe.modifications")}</h2>
          <p>{splitText(recipe.modifications)}</p>

          {allowedToEdit() ? (
            <button
              className="pure-button pure-button-primary"
              onClick={editRecipe}
            >
              {t("recipe.edit")}
            </button>
          ) : null}
        </div>
      </>
    );
  }
}
