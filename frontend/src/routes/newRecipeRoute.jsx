import EditRecipeForm from "../components/editRecipeForm.jsx";
import { Recipe } from "../model/recipe.js";

export default function NewRecipeRoute() {
  return (
    <EditRecipeForm
      titelKey="newRecipe.title"
      initRecipe={
        new Recipe({
          title: "",
          category: "",
          cuisine: "",
          yields: "",
          ingredients: "",
          instructions: "",
          modifications: "",
        })
      }
    />
  );
}
