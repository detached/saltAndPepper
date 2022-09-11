package de.w3is.recipes.recipes.infra.view.model

data class RecipeListEntryModel(
        val id: String,
        val title: String,
        val author: String,
        val category: String,
        val cuisine: String,
        val imageUrl: String?,
        val recipeUrl: String
)