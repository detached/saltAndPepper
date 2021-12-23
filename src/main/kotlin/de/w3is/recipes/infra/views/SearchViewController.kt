package de.w3is.recipes.infra.views

import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.infra.api.toThumbnailUrl
import de.w3is.recipes.infra.views.model.Menu
import de.w3is.recipes.infra.views.model.RecipeListEntryModel
import de.w3is.recipes.infra.views.model.Site
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

const val SEARCH_PATH = "/search"
const val QUERY_PARAM = "query"
const val MAX_RESULTS_PARAM = "maxResults"
const val PAGE_PARAM = "page"

@Controller(SEARCH_PATH)
@Secured(SecurityRule.IS_AUTHENTICATED)
class SearchViewController(
    private val recipeRepository: RecipeRepository,
    private val authorRepository: AuthorRepository
) {

    @Get
    @View("search")
    fun getSearch(
        @QueryValue(QUERY_PARAM) query: String?,
        @QueryValue(MAX_RESULTS_PARAM) maxResults: Int?,
        @QueryValue(PAGE_PARAM) page: Int?
    ): HttpResponse<Map<String, *>> {

        val queryString = URLDecoder.decode(query.orEmpty(), "UTF-8")
        val encodedQueryString = URLEncoder.encode(queryString, "UTF-8")

        val searchResponse = recipeRepository.search(
            query = queryString,
            limit = maxResults ?: 10,
            page = page ?: 0
        )

        val model = mutableMapOf(
            "menu" to Menu(activeItem = Site.SEARCH),
            "recipes" to searchResponse.results.toListEntries(),
            "currentPageNumber" to searchResponse.page.current,
            "maxPageNumber" to searchResponse.page.max,
            "search" to queryString
        )

        if (searchResponse.page.current < searchResponse.page.max) {
            model["nextPageUrl"] = buildSearchUrl(
                queryString = encodedQueryString,
                maxResults = maxResults,
                page = searchResponse.page.current + 1
            )
            model["lastPageUrl"] = buildSearchUrl(
                queryString = encodedQueryString,
                maxResults = maxResults,
                page = searchResponse.page.max
            )
        }

        if (searchResponse.page.current > 0) {
            model["prevPageUrl"] = buildSearchUrl(
                queryString = encodedQueryString,
                maxResults = maxResults,
                page = searchResponse.page.current - 1
            )
            model["firstPageUrl"] = buildSearchUrl(
                queryString = encodedQueryString,
                maxResults = maxResults,
                page = 0
            )
        }

        return HttpResponse.ok(model)
    }

    private fun buildSearchUrl(queryString: String, maxResults: Int?, page: Int): String =
        UriBuilder.of(SEARCH_PATH)
            .queryParam(QUERY_PARAM, queryString)
            .queryParam(PAGE_PARAM, page)
            .apply {
                if (maxResults != null) {
                    this.queryParam(MAX_RESULTS_PARAM, maxResults)
                }
            }
            .build().toString()

    fun List<Recipe>.toListEntries() = map { recipe ->

        val author = authorRepository.get(recipe.authorId)

        RecipeListEntryModel(
            id = recipe.id.recipeId,
            author = author.name,
            title = recipe.title,
            category = recipe.category,
            cuisine = recipe.cuisine,
            imageUrl = recipe.getImages().firstOrNull()?.toThumbnailUrl(),
            recipeUrl = recipe.id.toUri().toString()
        )
    }.toList()
}

val searchUri = URI("/search")
