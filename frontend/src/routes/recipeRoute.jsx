import { useNavigate, useParams } from "react-router-dom";
import { useCallback, useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import ImageGallery from "../components/imageGallery";
import { SaltAndPepper } from "../api/saltAndPepper";
import { useProfile } from "../context/profileProvider";
import Modal from "../components/modal";
import "./recipeRoute.css";
import Comments from "../components/comments.jsx";

export default function RecipeRoute() {
  const { t } = useTranslation();
  const navigateTo = useNavigate();
  const { recipeId } = useParams();
  const [recipe, setRecipe] = useState(null);
  const profile = useProfile();
  const [showDeleteConfirmation, setShowDeleteConfirmation] = useState(false);

  useEffect(() => {
    SaltAndPepper.getRecipe(recipeId).then((result) => {
      setRecipe(result);
    });
  }, [recipeId, setRecipe]);

  function editRecipe() {
    navigateTo("/recipe/" + recipeId + "/edit");
  }

  const askForDeleteConfirmation = useCallback(() => {
    setShowDeleteConfirmation(true);
  }, [setShowDeleteConfirmation]);

  const closeDeleteConfirmation = useCallback(() => {
    setShowDeleteConfirmation(false);
  }, [setShowDeleteConfirmation]);

  const deleteRecipe = useCallback(() => {
    setShowDeleteConfirmation(false);
    SaltAndPepper.deleteRecipe(recipeId).then(() => {
      navigateTo("/");
    });
  }, [recipeId, setShowDeleteConfirmation, navigateTo]);

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
        {showDeleteConfirmation ? (
          <Modal
            headerText={t("deleteRecipe.title")}
            contentText={t("deleteRecipe.text")}
            buttonOneText={t("deleteRecipe.yes")}
            buttonOneCallback={deleteRecipe}
            buttonTwoText={t("deleteRecipe.no")}
            buttonTwoCallback={closeDeleteConfirmation}
          />
        ) : null}
        <div className="header">
          <h1>{recipe.title}</h1>
        </div>
        <div className="content centered">
          {allowedToEdit() ? (
            <div className="recipe-buttons">
              <button
                id="edit-recipe"
                className="pure-button pure-button-primary"
                onClick={editRecipe}
              >
                {t("recipe.edit")}
              </button>
              <button
                id="delete-recipe"
                className="pure-button"
                onClick={() => askForDeleteConfirmation()}
              >
                {t("recipe.delete")}
              </button>
            </div>
          ) : null}

          <ImageGallery images={recipe.images} />

          <div className="pure-g">
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

          <h2>{t("recipe.comments")}</h2>
          <Comments recipeId={recipe.id} />
        </div>
      </>
    );
  }
}
