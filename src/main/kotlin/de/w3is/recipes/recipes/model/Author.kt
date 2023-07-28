package de.w3is.recipes.recipes.model

data class AuthorId(val value: String)

data class Author(
    val id: AuthorId,
    val name: String,
)
