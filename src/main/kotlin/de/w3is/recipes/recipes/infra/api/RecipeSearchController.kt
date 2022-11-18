package de.w3is.recipes.recipes.infra.api

import de.w3is.recipes.images.infra.api.toThumbnailUrl
import de.w3is.recipes.recipes.AuthorRepository
import de.w3is.recipes.recipes.RecipeRepository
import de.w3is.recipes.recipes.model.Recipe
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/api/recipe/search")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeSearchController(
    private val recipeRepository: RecipeRepository,
    private val authorRepository: AuthorRepository,
) {

    @Post
    fun search(searchRequest: SearchRequest): SearchResponse {

        val limit = with(searchRequest.page.size) {
            if (this in 1..99) {
                this
            } else {
                20
            }
        }

        val page = with(searchRequest.page.number) {
            if (this >= 0) {
                this
            } else {
                0
            }
        }

        val searchResponse = recipeRepository.search(searchRequest.searchQuery, limit, page)

        return SearchResponse(
            page = Page(
                size = searchResponse.page.size,
                number = searchResponse.page.current,
                maxNumber = searchResponse.page.max,
            ),
            data = searchResponse.results.map { it.toViewModel() }
        )
    }

    private fun Recipe.toViewModel(): SearchResponseData {
        return SearchResponseData(
            id = this.id.recipeId,
            imageUrl = this.getImages().firstOrNull()?.toThumbnailUrl() ?: "",
            title = this.title,
            category = this.category,
            cuisine = this.cuisine,
            author = authorRepository.get(this.authorId).name
        )
    }
}