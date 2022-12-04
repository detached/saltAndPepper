import { useParams } from "react-router-dom";
import { useEffect, useState } from "react";
import { SaltAndPepper } from "../api/saltAndPepper";
import EditRecipeForm from "../components/editRecipeForm";

export default function EditRecipeRoute() {
  const { recipeId } = useParams();
  const [recipe, setRecipe] = useState(null);

  useEffect(() => {
    SaltAndPepper.getRecipe(recipeId).then((r) => setRecipe(r));
  }, [recipeId, setRecipe]);

  if (recipe) {
    return <EditRecipeForm titleKey="editRecipe.title" initRecipe={recipe} />;
  } else {
    return <p>Loading...</p>;
  }
}
