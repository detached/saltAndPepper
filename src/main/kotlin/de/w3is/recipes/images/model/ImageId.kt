package de.w3is.recipes.images.model

import io.micronaut.serde.annotation.Serdeable
import java.util.*

@Serdeable
data class ImageId(val value: String) {
    companion object {
        fun new(): ImageId = ImageId(UUID.randomUUID().toString())
    }
}
