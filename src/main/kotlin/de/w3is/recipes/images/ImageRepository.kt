package de.w3is.recipes.images

import java.io.InputStream

interface ImageRepository {

    fun store(imageId: ImageId, data: InputStream, thumbnail: InputStream)
    fun get(imageId: ImageId): InputStream
    fun getThumbnail(imageId: ImageId): InputStream
    fun delete(imageId: ImageId)
}