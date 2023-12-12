package de.w3is.recipes.recipes.infra.api

import de.w3is.recipes.images.infra.api.toThumbnailUrl
import de.w3is.recipes.recipes.AuthorRepository
import de.w3is.recipes.recipes.RecipeRepository
import de.w3is.recipes.recipes.model.AuthorId
import de.w3is.recipes.recipes.model.FilterKey
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.SearchRequest
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/recipe/search")
@Secured(SecurityRule.IS_AUTHENTICATED)
class RecipeSearchController(
    private val recipeRepository: RecipeRepository,
    private val authorRepository: AuthorRepository,
) {

    @Post
    @Operation(summary = "Search recipes", operationId = "searchRecipes")
    @Tag(name = "recipes")
    fun search(@Body searchRequestViewModel: SearchRequestViewModel): SearchResponseViewModel {
        val searchResponse = recipeRepository.search(searchRequestViewModel.toSearchRequest())
        val possibleFilter = searchResponse.possibleFilter.toViewModel()

        return with(searchResponse) {
            SearchResponseViewModel(
                page = PageViewModel(
                    size = page.size,
                    number = page.current,
                    maxNumber = page.max,
                ),
                data = results.map { it.toViewModel() },
                possibleFilter = possibleFilter,
            )
        }
    }

    private fun Recipe.toViewModel(): SearchResponseData {
        return SearchResponseData(
            id = this.id.recipeId,
            imageUrl = this.getImages().firstOrNull()?.toThumbnailUrl() ?: "",
            title = this.title,
            category = this.category,
            cuisine = this.cuisine,
            author = authorRepository.get(this.authorId).name,
        )
    }

    private fun SearchRequestViewModel.toSearchRequest(): SearchRequest {
        val limit = with(page.size) {
            if (this in 1..99) {
                this
            } else {
                20
            }
        }

        val page = with(page.number) {
            if (this >= 0) {
                this
            } else {
                0
            }
        }

        return SearchRequest(
            query = this.searchQuery,
            limit = limit,
            page = page,
            filter = this.filter,
            order = this.order,
        )
    }

    private fun Map<FilterKey, List<String>>.toViewModel(): Map<FilterKey, List<FilterValueViewModel>> {
        return this.mapValues { (key, values) ->
            when (key) {
                FilterKey.AUTHOR -> values.resolveAuthorNamesAsLabels()
                else -> values.useValueAsLabels()
            }
        }
    }

    private fun List<String>.resolveAuthorNamesAsLabels(): List<FilterValueViewModel> {
        val authors = authorRepository.get(this.map { AuthorId(it) }.toSet())

        return authors.map { author ->
            FilterValueViewModel(value = author.id.value, label = author.name)
        }
    }

    private fun List<String>.useValueAsLabels(): List<FilterValueViewModel> = map { FilterValueViewModel(it, it) }
}
