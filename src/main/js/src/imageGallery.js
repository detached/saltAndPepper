export function deleteRecipeImage(recipeId, imageId) {
    fetch('/recipe/' + recipeId + '/image/' + imageId, {
        method: 'DELETE'
    }).then(response => {
        if(response.ok) {
            window.location.href = '/recipe/' + recipeId;
        }
    })
}