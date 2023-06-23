import "./newRecipes.css";
import saltAndPepperIcon from "../res/saltAndPepper.png";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Page,
  SaltAndPepper,
  SearchRequest,
  SearchFilter,
  OrderField,
} from "../api/saltAndPepper";

export default function NewRecipes() {
  const navigateTo = useNavigate();
  const [items, setItems] = useState([]);

  useEffect(() => {
    SaltAndPepper.search(
      new SearchRequest(
        "",
        new Page(30, 0),
        new SearchFilter([], [], []),
        OrderField.CREATED_AT
      )
    ).then((result) => {
      setItems(result.data);
    });
  }, [setItems]);

  function navigateToRecipe(recipeId) {
    navigateTo("/recipe/" + recipeId);
  }

  return (
    <div className="newrecipes-box">
      {items.map((recipe) => (
        <div
          key={recipe.id}
          className="newrecipes-card"
          onClick={() => navigateToRecipe(recipe.id)}
        >
          <div id="image">
            {recipe.imageUrl ? (
              <img id="image" src={recipe.imageUrl} alt="" />
            ) : (
              <img
                id="image"
                src={saltAndPepperIcon}
                className="newrecipes-default-icon"
                alt=""
              />
            )}
          </div>
          <div id="content">
            <div id="title">{recipe.title}</div>
            <div id="subtitle">{recipe.author}</div>
          </div>
        </div>
      ))}
    </div>
  );
}
