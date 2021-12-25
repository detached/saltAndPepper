package de.w3is.recipes.infra.api

import de.w3is.recipes.domain.ImageRepository
import de.w3is.recipes.domain.model.ImageId
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/images")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ImagesController(private val imageRepository: ImageRepository) {

    @Get("/{id}", produces = [MediaType.IMAGE_PNG])
    fun getImage(@PathVariable id: String): HttpResponse<ByteArray> =
        HttpResponse.ok(imageRepository.get(ImageId(id)).readAllBytes()).cacheControl(31536000)

    @Get("/{id}/thumbnail", produces = [MediaType.IMAGE_PNG])
    fun getThumbnail(@PathVariable id: String): HttpResponse<ByteArray> =
        HttpResponse.ok(imageRepository.getThumbnail(ImageId(id)).readAllBytes()).cacheControl(31536000)
}

private fun <B> MutableHttpResponse<B>.cacheControl(maxAge: Int) = apply {
    header("Cache-Control", "max-age=$maxAge")
}

fun ImageId.toImageUrl() = "/api/images/${this.value}"
fun ImageId.toThumbnailUrl() = "/api/images/${this.value}/thumbnail"