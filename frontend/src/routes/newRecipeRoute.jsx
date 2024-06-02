import EditRecipeForm from "../components/editRecipeForm";
import { Recipe } from "../model/recipe";

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
