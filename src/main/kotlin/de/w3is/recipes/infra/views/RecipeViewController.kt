package de.w3is.recipes.infra.views

import de.w3is.recipes.application.NewRecipeCommand
import de.w3is.recipes.application.RecipeService
import de.w3is.recipes.application.UpdateRecipeCommand
import de.w3is.recipes.application.users.User
import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.infra.security.getUser
import de.w3is.recipes.infra.views.model.*
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI

@Controller("/recipe")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeViewController(
    private val translations: Translations,
    private val recipeService: RecipeService,
    private val authorRepository: AuthorRepository,
) {

    @Get("/{id}")
    @View("recipe")
    fun showRecipe(@PathVariable id: String, authentication: Authentication): HttpResponse<*> {

        val user = authentication.getUser()

        return HttpResponse.ok(
            mapOf(
                Pair("translations", translations),
                Pair("menu", Menu(activeItem = Site.RECIPE)),
                Pair("recipe", recipeViewModelFor(user, RecipeId(id))),
            )
        )
    }

    @Get("/{id}/edit")
    @View("editRecipe")
    fun editRecipePage(@PathVariable id: String, authentication: Authentication): HttpResponse<*> {

        val user = authentication.getUser()
        val recipe = recipeService.get(RecipeId(id))

        return if (recipe.authorId == user.toAuthor().id) {
            HttpResponse.ok(
                mapOf(
                    Pair("translations", translations),
                    Pair("menu", Menu(activeItem = Site.RECIPE)),
                    Pair("recipe", recipeViewModelFor(user, RecipeId(id)))
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
        val recipe = recipeService.updateRecipe(toUpdateRecipeCommand(id, formBody), user)

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
                    Pair("translations", translations),
                    Pair("menu", Menu(activeItem = Site.RECIPE)),
                    Pair("recipe", recipeViewModelFor(user, RecipeId(id))),
                    Pair("state", "confirm"),
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
                Pair("translations", translations),
                Pair("menu", Menu(activeItem = Site.RECIPE)),
                Pair("state", "deleted"),
            )
        )
    }

    @Get("/new")
    @View("newRecipe")
    fun newRecipePage(): HttpResponse<*> = HttpResponse.ok(
        mapOf(
            Pair("translations", translations),
            Pair("menu", Menu(activeItem = Site.RECIPE))
        )
    )

    @Post("/new", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    fun createNewRecipe(@Body formBody: Map<String, String>, authentication: Authentication): HttpResponse<Unit> {

        val user = authentication.getUser()

        val recipe = recipeService.createNewRecipe(toNewRecipeCommand(formBody), user)

        return HttpResponse.redirect(recipe.id.toUri())
    }

    private fun toUpdateRecipeCommand(id: String, formBody: Map<String, String>) = UpdateRecipeCommand(
        id = RecipeId(id),
        title = formBody["title"] ?: error("title was not set"),
        category = formBody["category"] ?: error("title was not set"),
        cuisine = formBody["cuisine"] ?: error("cuisine was not set"),
        yields = formBody["yields"] ?: error("yields was not set"),
        instructions = formBody["instructions"] ?: error("instructions was not set"),
        modifications = formBody["modifications"] ?: error("modifications was not set"),
        ingredients = formBody["ingredients"] ?: error("modifications was not set"),
    )

    private fun toNewRecipeCommand(formBody: Map<String, String>) = NewRecipeCommand(
        title = formBody["title"] ?: error("title was not set"),
        category = formBody["category"] ?: error("title was not set"),
        cuisine = formBody["cuisine"] ?: error("cuisine was not set"),
        yields = formBody["yields"] ?: error("yields was not set"),
        instructions = formBody["instructions"] ?: error("instructions was not set"),
        modifications = formBody["modifications"] ?: error("modifications was not set"),
        ingredients = formBody["ingredients"] ?: error("modifications was not set"),
    )

    private fun recipeViewModelFor(user: User, recipeId: RecipeId): RecipeViewModel =
        recipeService.get(recipeId).let { recipe ->
            val author = authorRepository.get(recipe.authorId)
            RecipeViewModel(
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
}

fun RecipeId.toUri(): URI = UriBuilder.of("/recipe").path(recipeId).build()
fun RecipeId.toEditUri(): URI = UriBuilder.of("/recipe").path(recipeId).path("edit").build()
fun RecipeId.toDeleteUri(): URI = UriBuilder.of("/recipe").path(recipeId).path("delete").build()