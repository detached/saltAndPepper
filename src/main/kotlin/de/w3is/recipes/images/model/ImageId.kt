package de.w3is.recipes.images.model

import java.util.*

data class ImageId(val value: String) {
    companion object {
        fun new(): ImageId = ImageId(UUID.randomUUID().toString())
    }
}
