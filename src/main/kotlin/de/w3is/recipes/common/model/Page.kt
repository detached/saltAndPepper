package de.w3is.recipes.common.model

import io.micronaut.serde.annotation.Serdeable

open class PageRequest(
    val limit: Int,
    val page: Int,
)

open class PageResponse<T>(
    val results: List<T>,
    val page: Page,
)

@Serdeable
data class Page(
    val current: Int,
    val max: Int,
    val size: Int,
)
