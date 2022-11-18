package de.w3is.recipes.recipes.infra.api

data class SearchRequest(
    val searchQuery: String,
    val page: Page
)

data class SearchResponse(
    val data: List<SearchResponseData>,
    val page: Page
)

data class SearchResponseData(
    val id: String,
    val imageUrl: String,
    val title: String,
    val category: String,
    val cuisine: String,
    val author: String,
)

data class Page(
    val size: Int,
    val number: Int,
    val maxNumber: Int?
)