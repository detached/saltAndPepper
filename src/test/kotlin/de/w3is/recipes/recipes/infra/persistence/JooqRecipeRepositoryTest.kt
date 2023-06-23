package de.w3is.recipes.recipes.infra.persistence

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import de.w3is.recipes.images.ImageId
import de.w3is.recipes.infra.persistence.generated.tables.Recipes.Companion.RECIPES
import de.w3is.recipes.recipes.model.*
import de.w3is.recipes.testUser
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Replaces
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@MicronautTest
class JooqRecipeRepositoryTest {

    @get:Bean
    @get:Replaces(Clock::class)
    val fixedClock: Clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

    @Inject
    private lateinit var recipeRepository: JooqRecipeRepository

    @Inject
    private lateinit var dslContext: DSLContext

    @BeforeEach
    fun setUp() {
        dslContext.truncate(RECIPES).execute()
    }

    @Test
    fun storeAndGetRecipe() {

        val expected = givenARecipe()

        recipeRepository.store(expected)
        val actual = recipeRepository.get(expected.id)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun storeAndGetAll() {

        val expected = (1..5).map { givenARecipe() }

        expected.forEach {
            recipeRepository.store(it)
        }

        val actual = recipeRepository.getAll().toList()

        assertThat(actual).containsOnly(*expected.toTypedArray())
    }

    @Test
    fun `when search for two words then connect them with and`() {

        val fooOne = givenARecipe(title = "foo", category = "one").also { recipeRepository.store(it) }
        givenARecipe(title = "foo", category = "two").also { recipeRepository.store(it) }
        givenARecipe(title = "bar").also { recipeRepository.store(it) }

        val response =
            recipeRepository.search(
                SearchRequest(
                    query = "foo one",
                    limit = 5,
                    page = 0,
                    filter = emptyMap(),
                    orderField = OrderField.TITLE
                )
            )

        assertThat(response.results).containsExactly(fooOne)
    }

    @Test
    fun `when search matches two recipes then return them both`() {

        val fooOne = givenARecipe(title = "foo", category = "one").also { recipeRepository.store(it) }
        val fooTwo = givenARecipe(title = "foo", category = "two").also { recipeRepository.store(it) }
        givenARecipe(title = "bar").also { recipeRepository.store(it) }

        val response =
            recipeRepository.search(
                SearchRequest(
                    query = "foo",
                    limit = 5,
                    page = 0,
                    filter = emptyMap(),
                    orderField = OrderField.TITLE
                )
            )

        assertThat(response.results).containsExactlyInAnyOrder(fooOne, fooTwo)
    }

    @Test
    fun `search in title, category and cuisine`() {

        val titleMatch = givenARecipe(title = "foo").also { recipeRepository.store(it) }
        val categoryMatch = givenARecipe(category = "foo").also { recipeRepository.store(it) }
        val cuisineMatch = givenARecipe(cuisine = "foo").also { recipeRepository.store(it) }

        val response =
            recipeRepository.search(
                SearchRequest(
                    query = "foo",
                    limit = 5,
                    page = 0,
                    filter = emptyMap(),
                    orderField = OrderField.TITLE
                )
            )

        assertThat(response.results).containsExactlyInAnyOrder(titleMatch, categoryMatch, cuisineMatch)
    }

    @Test
    fun `test pagination`() {

        (1..23).map { givenARecipe() }.forEach { recipeRepository.store(it) }

        val firstTen = recipeRepository.search(
            SearchRequest(
                query = "",
                limit = 10,
                page = 0,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )
        val secondTen = recipeRepository.search(
            SearchRequest(
                query = "",
                limit = 10,
                page = 1,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )
        val theRest = recipeRepository.search(
            SearchRequest(
                query = "",
                limit = 10,
                page = 2,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )

        assertThat(firstTen).all {
            transform { it.results }.hasSize(10)
            transform { it.page.size }.isEqualTo(10)
            transform { it.page.max }.isEqualTo(2)
            transform { it.page.current }.isEqualTo(0)
        }

        assertThat(secondTen).all {
            transform { it.results }.hasSize(10)
            transform { it.page.size }.isEqualTo(10)
            transform { it.page.max }.isEqualTo(2)
            transform { it.page.current }.isEqualTo(1)
        }

        assertThat(theRest).all {
            transform { it.results }.hasSize(3)
            transform { it.page.size }.isEqualTo(3)
            transform { it.page.max }.isEqualTo(2)
            transform { it.page.current }.isEqualTo(2)
        }
    }

    @Test
    fun `search results should be ordered alphabetically by title`() {

        val b = givenARecipe(title = "b").also { recipeRepository.store(it) }
        val a = givenARecipe(title = "a").also { recipeRepository.store(it) }
        val c = givenARecipe(title = "c").also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "",
                limit = 10,
                page = 0,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )

        assertThat(result.results).containsExactly(a, b, c)
    }

    @Test
    fun `when define empty filter then don't filter anything`() {

        val fooOne = givenARecipe(title = "foo", category = "one").also { recipeRepository.store(it) }

        val response =
            recipeRepository.search(
                SearchRequest(
                    query = "foo", limit = 5, page = 0, filter = mapOf(
                        FilterKey.CATEGORY to emptyList()
                    ),
                    orderField = OrderField.TITLE
                )
            )

        assertThat(response.results).containsExactly(fooOne)
    }

    @Test
    fun `search results contain all possible filters`() {

        givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "",
                limit = 10,
                page = 0,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b", "c"),
            FilterKey.CUISINE to listOf("x", "y", "z"),
            FilterKey.AUTHOR to listOf("1", "2", "3")
        )
    }

    @Test
    fun `search results possible filters are restricted by search query`() {

        givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "a",
                limit = 10,
                page = 0,
                filter = emptyMap(),
                orderField = OrderField.TITLE
            )
        )

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a"),
            FilterKey.CUISINE to listOf("x"),
            FilterKey.AUTHOR to listOf("1")
        )
    }

    @Test
    fun `setting a category filter reduces search results`() {

        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "", limit = 10, page = 0, filter = mapOf(
                    FilterKey.CATEGORY to listOf("a", "b")
                ),
                orderField = OrderField.TITLE
            )
        )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2")
        )
    }

    @Test
    fun `setting a cuisine filter reduces search results`() {

        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "", limit = 10, page = 0, filter = mapOf(
                    FilterKey.CUISINE to listOf("x", "y")
                ), orderField = OrderField.TITLE
            )
        )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2")
        )
    }

    @Test
    fun `setting a author filter reduces search results`() {

        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "", limit = 10, page = 0, filter = mapOf(
                    FilterKey.AUTHOR to listOf("1", "2")
                ), orderField = OrderField.TITLE
            )
        )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2")
        )
    }

    @Test
    fun `setting a filter and search query reduces search result`() {

        val recipe1 = givenARecipe(title = "recipe1", category = "a").also { recipeRepository.store(it) }
        val recipe2 = givenARecipe(title = "recipe2", category = "a").also { recipeRepository.store(it) }
        givenARecipe(title = "recipe3", category = "b").also { recipeRepository.store(it) }
        givenARecipe(title = "another", category = "a").also { recipeRepository.store(it) }

        val result = recipeRepository.search(
            SearchRequest(
                query = "recipe", limit = 10, page = 0, filter = mapOf(
                    FilterKey.CATEGORY to listOf("a")
                ), orderField = OrderField.TITLE
            )
        )

        assertThat(result.results).containsExactlyInAnyOrder(recipe1, recipe2)
    }

    private fun givenARecipe(
        title: String = "title",
        category: String = "category",
        cuisine: String = "cuisine",
        author: AuthorId = testUser.toAuthor().id
    ) =
        Recipe(
            id = RecipeId.new(),
            title = title,
            category = category,
            cuisine = cuisine,
            yields = "yields",
            ingredients = "ingredients",
            instructions = "instructions",
            modifications = "modifications",
            images = mutableListOf(ImageId.new()),
            authorId = author,
            createdAt = OffsetDateTime.now(fixedClock)
        )
}