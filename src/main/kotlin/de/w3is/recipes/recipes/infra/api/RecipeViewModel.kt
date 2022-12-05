package de.w3is.recipes.recipes.infra.api

data class NewRecipeRequest(
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String
)

data class NewRecipeResponse(
    val id: String
)

data class RecipeViewModel (
    val id: String,
    val author: AuthorViewModel,
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String,
    val images: List<ImageViewModel>,
)

data class AuthorViewModel (
    val id: String,
    val name: String
)

data class ImageViewModel(
    val id: String,
    val url: String?,
    val thumbnailUrl: String?
)