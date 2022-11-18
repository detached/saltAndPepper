package de.w3is.recipes.recipes.infra.api

import de.w3is.recipes.common.getUser
import de.w3is.recipes.images.infra.api.toImageUrl
import de.w3is.recipes.images.infra.api.toThumbnailUrl
import de.w3is.recipes.recipes.AuthorRepository
import de.w3is.recipes.recipes.RecipeContent
import de.w3is.recipes.recipes.RecipeService
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.UserService
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule

@Controller("/api/recipe")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeController(
    private val userService: UserService,
    private val recipeService: RecipeService,
    private val authorRepository: AuthorRepository,
) {

    @Post("/")
    fun storeRecipe(@Body request: NewRecipeRequest, authentication: Authentication): NewRecipeResponse {
        val user = with(userService) { authentication.getUser() }
        val recipe = recipeService.createNewRecipe(
            RecipeContent(
                title = request.title,
                category = request.category,
                cuisine = request.cuisine,
                yields = request.yields,
                ingredients = request.ingredients,
                instructions = request.instructions,
                modifications = request.modifications
            ), user
        )

        return NewRecipeResponse(recipe.id.recipeId)
    }

    @Get("/{id}")
    fun getRecipe(@PathVariable("id") recipeId: String): RecipeViewModel {
        return recipeService.get(RecipeId(recipeId)).toModel()
    }

    @Put("/{id}")
    fun updateRecipe(
        @PathVariable("id") recipeId: String,
        @Body request: RecipeViewModel,
        authentication: Authentication
    ): RecipeViewModel {
        val user = with(userService) { authentication.getUser() }
        val content = RecipeContent(
            title = request.title,
            category = request.category,
            cuisine = request.cuisine,
            yields = request.yields,
            ingredients = request.ingredients,
            instructions = request.instructions,
            modifications = request.modifications
        )
        return recipeService.updateRecipe(RecipeId(recipeId), content, user).toModel()
    }

    private fun Recipe.toModel(): RecipeViewModel {
        return RecipeViewModel(
            id = this.id.recipeId,
            author = AuthorViewModel(
                id = this.authorId.value,
                name = authorRepository.get(this.authorId).name
            ),
            title = this.title,
            category = this.category,
            cuisine = this.cuisine,
            yields = this.yields,
            ingredients = this.ingredients,
            instructions = this.instructions,
            modifications = this.modifications,
            images = this.getImages().map {
                ImageViewModel(
                    id = it.value,
                    url = it.toImageUrl(),
                    thumbnailUrl = it.toThumbnailUrl(),
                )
            }
        )
    }
}
