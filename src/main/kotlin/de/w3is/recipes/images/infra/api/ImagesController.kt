package de.w3is.recipes.images.infra.api

import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.images.model.ImageId
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/images")
@Secured(SecurityRule.IS_ANONYMOUS)
class ImagesController(private val imageRepository: ImageRepository) {

    @Get("/{id}", produces = [MediaType.IMAGE_PNG])
    @Operation(summary = "Get image by id", operationId = "getImage")
    @Tag(name = "images")
    fun getImage(@PathVariable("id") id: String): HttpResponse<ByteArray> =
        HttpResponse.ok(imageRepository.get(ImageId(id)).readAllBytes()).cacheControl(31536000)

    @Get("/{id}/thumbnail", produces = [MediaType.IMAGE_PNG])
    @Operation(summary = "Get thumbnail by id", operationId = "getThumbnail")
    @Tag(name = "images")
    fun getThumbnail(@PathVariable("id") id: String): HttpResponse<ByteArray> =
        HttpResponse.ok(imageRepository.getThumbnail(ImageId(id)).readAllBytes()).cacheControl(31536000)
}

private fun <B> MutableHttpResponse<B>.cacheControl(maxAge: Int) = apply {
    header("Cache-Control", "max-age=$maxAge")
}

fun ImageId.toImageUrl() = "/api/images/${this.value}"
fun ImageId.toThumbnailUrl() = "/api/images/${this.value}/thumbnail"
