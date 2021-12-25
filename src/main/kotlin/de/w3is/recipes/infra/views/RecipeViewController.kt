package de.w3is.recipes.infra.views

import de.w3is.recipes.application.RecipeContent
import de.w3is.recipes.application.RecipeService
import de.w3is.recipes.application.users.User
import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.infra.api.toImageUrl
import de.w3is.recipes.infra.security.getUser
import de.w3is.recipes.infra.views.model.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.multipart.CompletedFileUpload
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI

@Controller("/recipe")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeViewController(
    private val recipeService: RecipeService,
    private val authorRepository: AuthorRepository,
) {

    @Get("/{id}")
    @View("recipe")
    fun showRecipe(@PathVariable id: String, authentication: Authentication): HttpResponse<*> {

        val user = authentication.getUser()
        val recipe = recipeService.get(RecipeId(id))

        return HttpResponse.ok(
            mapOf(
                "menu" to Menu(activeItem = Site.RECIPE),
                "recipe" to getRecipeViewModelFor(user, recipe),
                "images" to getImageViewModelsFor(recipe)
            )
        )
    }

    @Post("/{id}/images", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun uploadRecipeImage(
        @PathVariable id: String,
        image: CompletedFileUpload,
        authentication: Authentication
    ): HttpResponse<Unit> {
        val user = authentication.getUser()
        val recipeId = RecipeId(id)
        recipeService.addImageToRecipe(recipeId, image.inputStream, user)
        return HttpResponse.redirect(recipeId.toUri())
    }

    @Delete("/{id}/image/{imageId}")
    fun removeRecipeImage(
        @PathVariable id: String,
        @PathVariable imageId: String,
        authentication: Authentication
    ): HttpResponse<Unit> {
        val user = authentication.getUser()
        val recipeId = RecipeId(id)
        recipeService.deleteImageFromRecipe(recipeId, ImageId(imageId), user)
        return HttpResponse.ok()
    }

    @Get("/{id}/edit")
    @View("editRecipe")
    fun editRecipePage(@PathVariable id: String, authentication: Authentication): HttpResponse<*> {

        val user = authentication.getUser()
        val recipe = recipeService.get(RecipeId(id))

        return if (recipe.authorId == user.toAuthor().id) {
            HttpResponse.ok(
                mapOf(
                    "menu" to Menu(activeItem = Site.RECIPE),
                    "recipe" to getRecipeViewModelFor(user, recipe)
                )
            )
        } else {
            HttpResponse.unauthorized<Map<String, String>>()
        }
    }

    @Post("/{id}/edit", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun editRecipe(
        @PathVariable id: String,
        @Body formBody: Map<String, String>,
        authentication: Authentication
    ): HttpResponse<Unit> {

        val user = authentication.getUser()
        val recipe = recipeService.updateRecipe(RecipeId(id), formBody.toRecipeContent(), user)

        return HttpResponse.redirect(recipe.id.toUri())
    }

    @Get("/{id}/delete")
    @View("deleteRecipe")
    fun deleteRecipePage(@PathVariable id: String, authentication: Authentication): HttpResponse<*> {

        val user = authentication.getUser()
        val recipe = recipeService.get(RecipeId(id))

        return if (recipe.authorId == user.toAuthor().id) {
            HttpResponse.ok(
                mapOf(
                    "menu" to Menu(activeItem = Site.RECIPE),
                    "recipe" to getRecipeViewModelFor(user, recipe),
                    "state" to "confirm",
                )
            )
        } else {
            HttpResponse.unauthorized<Map<String, String>>()
        }
    }

    @Post("/{id}/delete", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    @View("deleteRecipe")
    fun deleteRecipe(
        @PathVariable id: String,
        authentication: Authentication
    ): HttpResponse<*> {

        val user = authentication.getUser()
        recipeService.deleteRecipe(RecipeId(id), user)

        return HttpResponse.ok(
            mapOf(
                "menu" to Menu(activeItem = Site.RECIPE),
                "state" to "deleted",
            )
        )
    }

    @Get("/new")
    @View("newRecipe")
    fun newRecipePage(): HttpResponse<*> = HttpResponse.ok(
        mapOf(
            "menu" to Menu(activeItem = Site.RECIPE)
        )
    )

    @Post("/new", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun createNewRecipe(@Body formBody: Map<String, String>, authentication: Authentication): HttpResponse<Unit> {

        val user = authentication.getUser()

        val recipe = recipeService.createNewRecipe(formBody.toRecipeContent(), user)

        return HttpResponse.redirect(recipe.id.toUri())
    }

    private fun Map<String, String>.toRecipeContent() = RecipeContent(
        title = this["title"] ?: error("title was not set"),
        category = this["category"] ?: error("title was not set"),
        cuisine = this["cuisine"] ?: error("cuisine was not set"),
        yields = this["yields"] ?: error("yields was not set"),
        instructions = this["instructions"] ?: error("instructions was not set"),
        modifications = this["modifications"] ?: error("modifications was not set"),
        ingredients = this["ingredients"] ?: error("modifications was not set"),
    )

    private fun getRecipeViewModelFor(user: User, recipe: Recipe): RecipeViewModel {
        val author = authorRepository.get(recipe.authorId)
        return RecipeViewModel(
            id = recipe.id.recipeId,
            title = recipe.title,
            category = recipe.category,
            cuisine = recipe.cuisine,
            yields = recipe.yields,
            ingredients = recipe.ingredients,
            instructions = recipe.instructions,
            modifications = recipe.modifications,
            author = author.name,
            allowedToEdit = user.toAuthor().id == recipe.authorId,
            editUrl = recipe.id.toEditUri().toString(),
            deleteUrl = recipe.id.toDeleteUri().toString(),
        )
    }

    private fun getImageViewModelsFor(recipe: Recipe) =
        recipe.getImages().map {
            ImageViewModel(it.value, it.toImageUrl())
        }
}

fun RecipeId.toUri(): URI = UriBuilder.of("/recipe").path(recipeId).build()
fun RecipeId.toEditUri(): URI = UriBuilder.of("/recipe").path(recipeId).path("edit").build()
fun RecipeId.toDeleteUri(): URI = UriBuilder.of("/recipe").path(recipeId).path("delete").build()