package de.w3is.recipes.common.api

import de.w3is.recipes.common.model.Page
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class PageViewModel(
    val size: Int,
    val number: Int,
    val maxNumber: Int?,
)

fun Page.toPageViewModel() =
    PageViewModel(
        size = this.size,
        number = this.current,
        maxNumber = this.max,
    )
