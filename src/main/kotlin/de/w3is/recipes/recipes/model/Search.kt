package de.w3is.recipes.recipes.model

import de.w3is.recipes.common.model.Page
import de.w3is.recipes.common.model.PageRequest
import de.w3is.recipes.common.model.PageResponse
import io.micronaut.serde.annotation.Serdeable

class SearchRequest(
    val query: String,
    val filter: Map<FilterKey, List<String>>,
    val order: Order,
    limit: Int,
    page: Int,
) : PageRequest(limit, page)

class SearchResponse(
    val possibleFilter: Map<FilterKey, List<String>>,
    results: List<Recipe>,
    page: Page,
) : PageResponse<Recipe>(results, page)

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
