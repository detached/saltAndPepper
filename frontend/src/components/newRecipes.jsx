import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { SaltAndPepper } from "../api/saltAndPepper.js";
import {
  Page,
  SearchRequest,
  SearchFilter,
  Order,
  OrderField,
  SortDir,
} from "../api/model.js";
import saltAndPepperIcon from "../res/saltAndPepper.png";
import "./newRecipes.css";

export default function NewRecipes() {
  const navigateTo = useNavigate();
  const [items, setItems] = useState([]);

  useEffect(() => {
    SaltAndPepper.search(
      new SearchRequest({
        searchQuery: "",
        page: new Page({ size: 30, number: 0 }),
        filter: new SearchFilter(),
        order: new Order({
          field: OrderField.CREATED_AT,
          direction: SortDir.DESC,
        }),
      }),
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
