package de.w3is.recipes.recipes.infra.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NewRecipeRequest(
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String,
)

@Serdeable
data class NewRecipeResponse(
    val id: String,
)

@Serdeable
data class RecipeViewModel(
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

@Serdeable
data class AuthorViewModel(
    val id: String,
    val name: String,
)

@Serdeable
data class ImageViewModel(
    val id: String,
    val url: String?,
    val thumbnailUrl: String?,
)
