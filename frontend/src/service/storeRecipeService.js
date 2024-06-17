import {SaltAndPepper} from "../api/saltAndPepper.js";
import {NewRecipeRequest} from "../api/model.js";
import FileResizer from "react-image-file-resizer";

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
        new NewRecipeRequest({
            title: recipe.title,
            category: recipe.category,
            cuisine: recipe.cuisine,
            yields: recipe.yields,
            ingredients: recipe.ingredients,
            instructions: recipe.instructions,
            modifications: recipe.modifications,
        }),
    );

    if (recipe.newImages !== undefined) {
        await Promise.all(
            recipe.newImages.map((image) =>
                resize(image).then((resized) => SaltAndPepper.addImageToRecipe(createdRecipe.id, resized))
            ),
        );
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
        newlyAddedImages = await Promise.all(
            recipe.newImages.map((image) =>
                resize(image).then((resized) => SaltAndPepper.addImageToRecipe(recipe.id, resized))
            ),
        );
    }
    delete recipe.newImages;

    recipe.images = recipe.images.concat(newlyAddedImages);

    return SaltAndPepper.updateRecipe(recipe);
}

const resize = (image) => {
    return new Promise((resolve) =>
        FileResizer.imageFileResizer(image, 800, 600, "PNG", 100, 0, (uri) => {
            resolve(uri)
        }, "blob")
    );
};