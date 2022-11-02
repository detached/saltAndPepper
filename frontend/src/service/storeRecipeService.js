import {NewRecipeRequest, SaltAndPepper} from "../api/saltAndPepper";

/**
 * @param recipe {Recipe}
 * @returns {Promise<*>}
 */
export async function storeRecipe(recipe) {
    if (recipe.id === undefined) {
        return addNewRecipe(recipe);
    } else {
        return updateRecipe(recipe);
    }
}

/**
 *
 * @param recipe {Recipe}
 * @returns {Promise<*>}
 */
async function addNewRecipe(recipe) {
    let createdRecipe = await SaltAndPepper.newRecipe(
        new NewRecipeRequest(
            recipe.title,
            recipe.category,
            recipe.cuisine,
            recipe.yields,
            recipe.ingredients,
            recipe.instructions,
            recipe.modifications)
    );

    if (recipe.newImages !== undefined) {
            await Promise.all(recipe.newImages.map(image =>
                SaltAndPepper.addImageToRecipe(createdRecipe.id, image)
            ));
    }

    return SaltAndPepper.getRecipe(createdRecipe.id);
}

/**
 * @param recipe {Recipe}
 * @returns {Promise<*>}
 */
async function updateRecipe(recipe) {

    let newlyAddedImages = [];
    if (recipe.newImages !== undefined) {
        newlyAddedImages = await Promise.all(recipe.newImages.map(image => SaltAndPepper.addImageToRecipe(recipe.id, image)));
    }

    recipe.images = recipe.images.concat((newlyAddedImages));

    return SaltAndPepper.updateRecipe(recipe);
}