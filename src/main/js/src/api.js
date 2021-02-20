export function getImagesOfRecipe(recipeId) {
    return fetch('/api/recipe/' + recipeId + '/images');
}

export function addImageToRecipe(recipeId, image) {
    const formData = new FormData();
    formData.append('image', image);
    return fetch('/api/recipe/' + recipeId + '/images', {
        method: 'POST', body: formData
    });
}

export function removeImageFromRecipe(recipeId, imageId) {
    return fetch('/api/recipe/' + recipeId + '/image/' + imageId, {
      method: 'DELETE'
    });
}
