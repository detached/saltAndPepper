package de.w3is.recipes.recipes.model

data class SearchRequest(
    val query: String,
    val limit: Int,
    val page: Int,
    val filter: Map<FilterKey, List<String>>
)

data class SearchResponse(
    val results: List<Recipe>,
    val page: Page,
    val possibleFilter: Map<FilterKey, List<String>>
)

data class Page(
    val current: Int,
    val max: Int,
    val size: Int,
)

enum class FilterKey {
    AUTHOR,
    CATEGORY,
    CUISINE
}