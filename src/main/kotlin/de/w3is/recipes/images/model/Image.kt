package de.w3is.recipes.images

import java.util.*

data class ImageId(val value: String) {
    companion object {
        fun new(): ImageId = ImageId(UUID.randomUUID().toString())
    }
}