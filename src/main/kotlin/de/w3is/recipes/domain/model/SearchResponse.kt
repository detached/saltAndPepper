package de.w3is.recipes.domain.model

data class SearchResponse(
    val results: List<Recipe>,
    val page: Page,
)

data class Page(
    val current: Int,
    val max: Int,
    val size: Int,
)