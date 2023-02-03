import { useTranslation } from "react-i18next";
import { useNavigate } from "react-router-dom";
import { useCallback, useEffect, useReducer, useState } from "react";
import { useDropzone } from "react-dropzone";
import { FaTrashAlt } from "react-icons/fa";
import { storeRecipe } from "../service/storeRecipeService";
import "./editRecipeForm.css";
import "./dropzone.css";
import Spinner from "./spinner";

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
    recipeReducer
  );

  const [images, setImages] = useState(
    initRecipe.images ? initRecipe.images : []
  );

  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      "image/jpeg": [".jpg", ".jpeg"],
      "image/png": [".png"],
    },
    onDrop: (acceptedFiles) => {
      let newImages = acceptedFiles.map((file) =>
        Object.assign(file, {
          isNew: true,
          thumbnailUrl: URL.createObjectURL(file),
        })
      );
      setImages(images.concat(newImages));
      recipeDispatcher({
        type: EditRecipeActions.ADD_NEW_IMAGES,
        value: acceptedFiles,
      });
    },
  });

  function removeImage(imageToRemove) {
    if (imageToRemove.isNew) {
      recipeDispatcher({
        type: EditRecipeActions.REMOVE_NEW_IMAGE,
        value: imageToRemove,
      });
    } else {
      recipeDispatcher({
        type: EditRecipeActions.REMOVE_EXISTING_IMAGE,
        value: imageToRemove,
      });
    }
    setImages(images.filter((image) => image !== imageToRemove));
  }

  const thumbs = images.map((file) => (
    <div
      className="edit-recipe-images-thumb"
      key={file.name ? file.name : file.id}
    >
      <div onClick={() => removeImage(file)}>
        <img
          id="image"
          src={file.thumbnailUrl}
          onLoad={() => {
            if (file.isNew) {
              URL.revokeObjectURL(file.thumbnailUrl);
            }
          }}
          alt="thumbnail"
        />
        <div id="overlay">
          <FaTrashAlt />
        </div>
      </div>
    </div>
  ));
  useEffect(() => {
    // Make sure to revoke the data uris to avoid memory leaks, will run on unmount
    return () =>
      images.forEach((file) => {
        if (file.isNew) {
          URL.revokeObjectURL(file.preview);
        }
      });
  }, [images]);

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
    [recipe, isLoading, navigateTo]
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
            <section className="edit-recipe-images-container">
              <div {...getRootProps({ className: "dropzone" })}>
                <input {...getInputProps()} />
                <p>{t("newRecipe.dropzone")}</p>
              </div>
              <aside className="edit-recipe-images-thumbs-container">
                {thumbs}
              </aside>
            </section>
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
