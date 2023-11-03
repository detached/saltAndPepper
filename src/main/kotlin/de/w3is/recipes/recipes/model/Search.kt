package de.w3is.recipes.recipes.model

import io.micronaut.serde.annotation.Serdeable

data class SearchRequest(
    val query: String,
    val limit: Int,
    val page: Int,
    val filter: Map<FilterKey, List<String>>,
    val order: Order,
)

data class SearchResponse(
    val results: List<Recipe>,
    val page: Page,
    val possibleFilter: Map<FilterKey, List<String>>,
)

@Serdeable
data class Page(
    val current: Int,
    val max: Int,
    val size: Int,
)

@Serdeable
enum class FilterKey {
    AUTHOR,
    CATEGORY,
    CUISINE,
}

@Serdeable
data class Order(
    val field: OrderField,
    val direction: SortDir,
)

@Serdeable
enum class OrderField {
    TITLE,
    CREATED_AT,
}

@Serdeable
enum class SortDir {
    ASC,
    DESC,
}
