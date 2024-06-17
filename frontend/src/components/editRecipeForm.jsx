import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useCallback, useReducer, useState } from "react";
import Spinner from "./spinner.jsx";
import ImageDropzone from "./imageDropzone.jsx";
import { storeRecipe } from "../service/storeRecipeService.js";
import "./editRecipeForm.css";
import "./dropzone.css";
import PropTypes from "prop-types";
import { Recipe } from "../model/recipe.js";

const EditRecipeActions = {
  SET_TITLE: "SET_TITLE",
  SET_CATEGORY: "SET_CATEGORY",
  SET_CUISINE: "SET_CUISINE",
  SET_YIELDS: "SET_YIELDS",
  SET_INGREDIENTS: "SET_INGREDIENTS",
  SET_INSTRUCTIONS: "SET_INSTRUCTIONS",
  SET_MODIFICATIONS: "SET_MODIFICATIONS",
  ADD_NEW_IMAGES: "ADD_NEW_IMAGE",
  REMOVE_NEW_IMAGE: "REMOVE_NEW_IMAGE",
  REMOVE_EXISTING_IMAGE: "REMOVE_EXISTING_IMAGE",
};

function recipeReducer(state, action) {
  switch (action?.type) {
    case EditRecipeActions.SET_TITLE:
      return {
        ...state,
        title: action.value,
      };
    case EditRecipeActions.SET_CATEGORY:
      return {
        ...state,
        category: action.value,
      };
    case EditRecipeActions.SET_CUISINE:
      return {
        ...state,
        cuisine: action.value,
      };
    case EditRecipeActions.SET_YIELDS:
      return {
        ...state,
        yields: action.value,
      };
    case EditRecipeActions.SET_INGREDIENTS:
      return {
        ...state,
        ingredients: action.value,
      };
    case EditRecipeActions.SET_INSTRUCTIONS:
      return {
        ...state,
        instructions: action.value,
      };
    case EditRecipeActions.SET_MODIFICATIONS:
      return {
        ...state,
        modifications: action.value,
      };
    case EditRecipeActions.ADD_NEW_IMAGES:
      return {
        ...state,
        newImages: state.newImages
          ? state.newImages.concat(action.value)
          : action.value,
      };
    case EditRecipeActions.REMOVE_NEW_IMAGE:
      return {
        ...state,
        newImages: state.newImages
          ? state.newImages.filter((image) => image !== action.value)
          : [],
      };
    case EditRecipeActions.REMOVE_EXISTING_IMAGE:
      return {
        ...state,
        images: state.images
          ? state.images.filter((image) => image.id !== action.value.id)
          : [],
      };

    default:
      return state;
  }
}

export default function EditRecipeForm({ titleKey, initRecipe }) {
  const { t } = useTranslation();
  const navigateTo = useNavigate();

  const [isLoading, setIsLoading] = useState(false);
  const [recipe, recipeDispatcher] = useReducer(
    recipeReducer,
    initRecipe,
    recipeReducer,
  );

  const handleImagesDropped = (acceptedFiles) => {
    recipeDispatcher({
      type: EditRecipeActions.ADD_NEW_IMAGES,
      value: acceptedFiles,
    });
  };

  const handleImageRemoved = (removedImage, wasNew) => {
    if (wasNew) {
      recipeDispatcher({
        type: EditRecipeActions.REMOVE_NEW_IMAGE,
        value: removedImage,
      });
    } else {
      recipeDispatcher({
        type: EditRecipeActions.REMOVE_EXISTING_IMAGE,
        value: removedImage,
      });
    }
  };

  const handleStoreRecipe = useCallback(
      (e) => {
        e.preventDefault();
        if (isLoading) {
          return;
        }
        setIsLoading(true);

        storeRecipe(recipe)
            .then((result) => {
              navigateTo("/recipe/" + result.id);
            })
            .finally(() => {
              setIsLoading(false);
            });
      },
      [recipe, isLoading, navigateTo],
  );

  return (
    <>
      {isLoading ? <Spinner fixedCentered="true" /> : null}
      <div className="header">
        <h1>{t(titleKey)}</h1>
      </div>
      <div className="content pure-g">
        <form className="pure-form pure-form-stacked">
          <div className="pure-u-1 pure-u-md-1-2">
            <ImageDropzone initImages={initRecipe.images} onImagesDropped={handleImagesDropped} onImageRemoved={handleImageRemoved}/>
          </div>
          <div className="pure-u-1 pure-u-md-1-2">
            <fieldset>
              <label htmlFor="title">{t("newRecipe.name")}:</label>
              <input
                type="text"
                id="title"
                name="title"
                placeholder={t("newRecipe.name")}
                className="pure-input-1"
                value={recipe.title}
                onChange={(e) =>
                  recipeDispatcher({
                    type: EditRecipeActions.SET_TITLE,
                    value: e.target.value,
                  })
                }
              />
              <label htmlFor="category">{t("recipe.category")}:</label>
              <input
                type="text"
                id="category"
                name="category"
                placeholder={t("recipe.category")}
                className="pure-input-1"
                value={recipe.category}
                onChange={(e) =>
                  recipeDispatcher({
                    type: EditRecipeActions.SET_CATEGORY,
                    value: e.target.value,
                  })
                }
              />
              <label htmlFor="cuisine">{t("recipe.cuisine")}:</label>
              <input
                type="text"
                id="cuisine"
                name="cuisine"
                placeholder={t("recipe.cuisine")}
                className="pure-input-1"
                value={recipe.cuisine}
                onChange={(e) =>
                  recipeDispatcher({
                    type: EditRecipeActions.SET_CUISINE,
                    value: e.target.value,
                  })
                }
              />
              <label htmlFor="yields">{t("recipe.yields")}:</label>
              <input
                type="text"
                id="yields"
                name="yields"
                placeholder={t("recipe.yields")}
                className="pure-input-1"
                value={recipe.yields}
                onChange={(e) =>
                  recipeDispatcher({
                    type: EditRecipeActions.SET_YIELDS,
                    value: e.target.value,
                  })
                }
              />
            </fieldset>
          </div>
          <div className="pure-u-1">
            <h2>{t("recipe.ingredients")}</h2>
            <textarea
              id="ingredients"
              name="ingredients"
              className="edit-recipe-textarea edit-recipe-texterea-big"
              placeholder={t("recipe.ingredients")}
              value={recipe.ingredients}
              onChange={(e) =>
                recipeDispatcher({
                  type: EditRecipeActions.SET_INGREDIENTS,
                  value: e.target.value,
                })
              }
            ></textarea>

            <h2>{t("recipe.instructions")}</h2>
            <textarea
              id="instructions"
              name="instructions"
              className="edit-recipe-textarea edit-recipe-texterea-big"
              placeholder={t("recipe.instructions")}
              value={recipe.instructions}
              onChange={(e) =>
                recipeDispatcher({
                  type: EditRecipeActions.SET_INSTRUCTIONS,
                  value: e.target.value,
                })
              }
            ></textarea>

            <h2>{t("recipe.modifications")}</h2>
            <textarea
              id="modifications"
              name="modifications"
              className="edit-recipe-textarea"
              placeholder={t("recipe.modifications")}
              value={recipe.modifications}
              onChange={(e) =>
                recipeDispatcher({
                  type: EditRecipeActions.SET_MODIFICATIONS,
                  value: e.target.value,
                })
              }
            ></textarea>

            <button
              type="submit"
              className="pure-button pure-button-primary edit-recipe-submit"
              disabled={isLoading}
              onClick={(e) => handleStoreRecipe(e)}
            >
              {t("newRecipe.submit")}
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
EditRecipeForm.propTypes = {
  titleKey: PropTypes.string,
  initRecipe: PropTypes.instanceOf(Recipe),
};
