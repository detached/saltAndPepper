package de.w3is.recipes.recipes.infra.api

import de.w3is.recipes.common.getUser
import de.w3is.recipes.images.infra.api.toImageUrl
import de.w3is.recipes.images.infra.api.toThumbnailUrl
import de.w3is.recipes.images.model.ImageId
import de.w3is.recipes.recipes.AuthorRepository
import de.w3is.recipes.recipes.RecipeContent
import de.w3is.recipes.recipes.RecipeService
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File

@Controller("/api/recipe")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeController(
    private val userService: UserService,
    private val recipeService: RecipeService,
    private val authorRepository: AuthorRepository,
) {
    @Post("/")
    fun storeRecipe(
        @Body request: NewRecipeRequest,
        authentication: Authentication,
    ): NewRecipeResponse {
        val user = with(userService) { authentication.getUser() }
        val recipe =
            recipeService.createNewRecipe(
                RecipeContent(
                    title = request.title,
                    category = request.category,
                    cuisine = request.cuisine,
                    yields = request.yields,
                    ingredients = request.ingredients,
                    instructions = request.instructions,
                    modifications = request.modifications,
                    images = emptyList(),
                ),
                user,
            )

        return NewRecipeResponse(recipe.id.recipeId)
    }

    @Get("/{id}")
    fun getRecipe(
        @PathVariable("id") recipeId: String,
    ): RecipeViewModel {
        return recipeService.get(RecipeId(recipeId)).toModel()
    }

    @Delete("/{id}")
    fun deleteRecipe(
        @PathVariable("id") recipeId: String,
        authentication: Authentication,
    ) {
        val user = with(userService) { authentication.getUser() }
        recipeService.deleteRecipe(RecipeId(recipeId), user)
    }

    @Put("/{id}")
    fun updateRecipe(
        @PathVariable("id") recipeId: String,
        @Body request: RecipeViewModel,
        authentication: Authentication,
    ): RecipeViewModel {
        val user = with(userService) { authentication.getUser() }
        val content =
            RecipeContent(
                title = request.title,
                category = request.category,
                cuisine = request.cuisine,
                yields = request.yields,
                ingredients = request.ingredients,
                instructions = request.instructions,
                modifications = request.modifications,
                images = request.images.map { ImageId(it.id) },
            )
        return recipeService.updateRecipe(RecipeId(recipeId), content, user).toModel()
    }

    @Post("/{id}/images", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun addImageToRecipe(
        @PathVariable("id") recipeId: String,
        file: StreamingFileUpload,
        authentication: Authentication,
    ): Mono<HttpResponse<*>> {
        val user = with(userService) { authentication.getUser() }

        if (file.definedSize > MAX_UPLOAD_SIZE) {
            return Mono.fromCallable { HttpResponse.badRequest<Unit>() }
        }

        return Mono.fromCallable { File.createTempFile("upload", "temp") }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { tempFile ->
                Mono.from(file.transferTo(tempFile)).map { success ->
                    tempFile to success
                }
            }.map { (tempFile, success) ->
                if (success) {
                    val imageId = recipeService.addImageToRecipe(RecipeId(recipeId), tempFile.inputStream(), user)
                    tempFile.delete()
                    HttpResponse.ok(imageId.toImageViewModel())
                } else {
                    HttpResponse.serverError("Could not store image")
                }
            }
    }

    private fun Recipe.toModel(): RecipeViewModel {
        return RecipeViewModel(
            id = this.id.recipeId,
            author =
                AuthorViewModel(
                    id = this.authorId.value,
                    name = authorRepository.get(this.authorId).name,
                ),
            title = this.title,
            category = this.category,
            cuisine = this.cuisine,
            yields = this.yields,
            ingredients = this.ingredients,
            instructions = this.instructions,
            modifications = this.modifications,
            images = this.getImages().map { it.toImageViewModel() },
        )
    }

    private fun ImageId.toImageViewModel() =
        ImageViewModel(
            id = value,
            url = toImageUrl(),
            thumbnailUrl = toThumbnailUrl(),
        )

    companion object {
        const val MAX_UPLOAD_SIZE = 4 * 1024 * 1024
    }
}
