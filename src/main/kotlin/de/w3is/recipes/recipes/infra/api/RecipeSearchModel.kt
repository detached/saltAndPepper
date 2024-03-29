package de.w3is.recipes.recipes.infra.api

import de.w3is.recipes.recipes.model.FilterKey
import de.w3is.recipes.recipes.model.Order
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class SearchRequestViewModel(
    val searchQuery: String,
    val page: PageViewModel,
    val filter: Map<FilterKey, List<String>>,
    val order: Order,
)

@Serdeable
data class SearchResponseViewModel(
    val data: List<SearchResponseData>,
    val page: PageViewModel,
    val possibleFilter: Map<FilterKey, List<FilterValueViewModel>>,
)

@Serdeable
data class SearchResponseData(
    val id: String,
    val imageUrl: String,
    val title: String,
    val category: String,
    val cuisine: String,
    val author: String,
)

@Serdeable
data class PageViewModel(
    val size: Int,
    val number: Int,
    val maxNumber: Int?,
)

@Serdeable
data class FilterValueViewModel(
    val value: String,
    val label: String,
)
