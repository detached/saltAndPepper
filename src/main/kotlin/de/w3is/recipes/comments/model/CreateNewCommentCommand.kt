package de.w3is.recipes.comments.model

import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.UserId

data class CreateNewCommentCommand(
    val userId: UserId,
    val recipeId: RecipeId,
    val comment: String,
)
