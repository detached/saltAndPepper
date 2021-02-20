package de.w3is.recipes.infra.views

import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.infra.api.toThumbnailUrl
import de.w3is.recipes.infra.views.model.Menu
import de.w3is.recipes.infra.views.model.RecipeListEntryModel
import de.w3is.recipes.infra.views.model.Site
import de.w3is.recipes.infra.views.model.Translations
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI
import java.net.URLDecoder
import java.net.URLEncoder

@Controller("/search")
@Secured(SecurityRule.IS_AUTHENTICATED)
class SearchViewController(
    private val translations: Translations,
    private val recipeRepository: RecipeRepository,
    private val authorRepository: AuthorRepository
) {

    @Get
    @View("search")
    fun getSearch(@QueryValue query: String?): HttpResponse<Map<String, *>> {

        val queryString = URLDecoder.decode(query.orEmpty(), "UTF-8")

        val recipes = recipeRepository.search(queryString, maxResults = 50)

        return HttpResponse.ok(
            mapOf(
                Pair("translations", translations),
                Pair("menu", Menu(activeItem = Site.SEARCH)),
                Pair("recipes", toListEntries(recipes)),
                Pair("search", URLEncoder.encode(queryString, "UTF-8"))
            )
        )
    }

    fun toListEntries(recipes: Sequence<Recipe>) = recipes.map { recipe ->

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
