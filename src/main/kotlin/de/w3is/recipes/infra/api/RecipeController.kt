package de.w3is.recipes.infra.api

import de.w3is.recipes.application.RecipeService
import de.w3is.recipes.application.importer.ImportService
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.infra.api.model.RecipeImage
import de.w3is.recipes.infra.importer.GourmetRecipeSource
import de.w3is.recipes.infra.security.getUser
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.reactivex.Single
import java.io.File

@Controller("/api/recipe")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeController(
    private val recipeService: RecipeService,
    private val importService: ImportService
) {

    @Get("/")
    fun getAll(): Sequence<Recipe> = recipeService.getAll()

    @Get("/{id}")
    fun getRecipe(@PathVariable id: String): Recipe = recipeService.get(RecipeId(id))

    @Get("/{id}/images")
    fun getRecipeImages(@PathVariable id: String): List<RecipeImage> =
        recipeService.get(RecipeId(id)).getImages().map {
            RecipeImage(it.value, it.toImageUrl())
        }

    @Post("/{id}/images", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun uploadRecipeImage(@PathVariable id: String, image: CompletedFileUpload, authentication: Authentication): RecipeImage {
        val user = authentication.getUser()
        val newImage = recipeService.addImageToRecipe(RecipeId(id), image.inputStream, user)
        return RecipeImage(newImage.value, newImage.toImageUrl())
    }

    @Delete("/{id}/image/{imageId}")
    fun removeRecipeImage(@PathVariable id: String, @PathVariable imageId: String, authentication: Authentication): HttpResponse<Unit> {
        val user = authentication.getUser()
        recipeService.deleteImageFromRecipe(RecipeId(id), ImageId(imageId), user)
        return HttpResponse.ok()
    }
}