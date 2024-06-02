package de.w3is.recipes.recipes.infra.persistence

import assertk.all
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.containsExactlyInAnyOrder
import assertk.assertions.containsOnly
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import de.w3is.recipes.images.model.ImageId
import de.w3is.recipes.infra.persistence.generated.tables.Recipes.Companion.RECIPES
import de.w3is.recipes.recipes.model.AuthorId
import de.w3is.recipes.recipes.model.FilterKey
import de.w3is.recipes.recipes.model.Order
import de.w3is.recipes.recipes.model.OrderField
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.recipes.model.SearchRequest
import de.w3is.recipes.recipes.model.SortDir
import de.w3is.recipes.testUser
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
    private val fixedClock: Clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

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
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
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
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
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
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(response.results).containsExactlyInAnyOrder(titleMatch, categoryMatch, cuisineMatch)
    }

    @Test
    fun `test pagination`() {
        (1..23).map { givenARecipe() }.forEach { recipeRepository.store(it) }

        val firstTen =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )
        val secondTen =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 1,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )
        val theRest =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 2,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
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
    fun `search results should be ordered alphabetically by title asc`() {
        val b = givenARecipe(title = "b").also { recipeRepository.store(it) }
        val a = givenARecipe(title = "a").also { recipeRepository.store(it) }
        val c = givenARecipe(title = "c").also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.ASC),
                ),
            )

        assertThat(result.results).containsExactly(a, b, c)
    }

    @Test
    fun `search results should be ordered alphabetically by title desc`() {
        val b = givenARecipe(title = "b").also { recipeRepository.store(it) }
        val a = givenARecipe(title = "a").also { recipeRepository.store(it) }
        val c = givenARecipe(title = "c").also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactly(c, b, a)
    }

    @Test
    fun `search results should be ordered by date desc`() {
        val b =
            givenARecipe(
                title = "b",
                createdAt = OffsetDateTime.of(2023, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }
        val a =
            givenARecipe(
                title = "a",
                createdAt = OffsetDateTime.of(2023, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }
        val c =
            givenARecipe(
                title = "c",
                createdAt = OffsetDateTime.of(2023, 7, 9, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.CREATED_AT, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactly(c, b, a)
    }

    @Test
    fun `search results should be ordered by date asc`() {
        val b =
            givenARecipe(
                title = "b",
                createdAt = OffsetDateTime.of(2023, 7, 5, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }
        val a =
            givenARecipe(
                title = "a",
                createdAt = OffsetDateTime.of(2023, 7, 1, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }
        val c =
            givenARecipe(
                title = "c",
                createdAt = OffsetDateTime.of(2023, 7, 9, 0, 0, 0, 0, ZoneOffset.UTC),
            ).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.CREATED_AT, direction = SortDir.ASC),
                ),
            )

        assertThat(result.results).containsExactly(a, b, c)
    }

    @Test
    fun `when define empty filter then don't filter anything`() {
        val fooOne = givenARecipe(title = "foo", category = "one").also { recipeRepository.store(it) }

        val response =
            recipeRepository.search(
                SearchRequest(
                    query = "foo",
                    limit = 5,
                    page = 0,
                    filter =
                        mapOf(
                            FilterKey.CATEGORY to emptyList(),
                        ),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(response.results).containsExactly(fooOne)
    }

    @Test
    fun `search results contain all possible filters`() {
        givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b", "c"),
            FilterKey.CUISINE to listOf("x", "y", "z"),
            FilterKey.AUTHOR to listOf("1", "2", "3"),
        )
    }

    @Test
    fun `search results possible filters are restricted by search query`() {
        givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "a",
                    limit = 10,
                    page = 0,
                    filter = emptyMap(),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a"),
            FilterKey.CUISINE to listOf("x"),
            FilterKey.AUTHOR to listOf("1"),
        )
    }

    @Test
    fun `setting a category filter reduces search results`() {
        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter =
                        mapOf(
                            FilterKey.CATEGORY to listOf("a", "b"),
                        ),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2"),
        )
    }

    @Test
    fun `setting a cuisine filter reduces search results`() {
        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter =
                        mapOf(
                            FilterKey.CUISINE to listOf("x", "y"),
                        ),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2"),
        )
    }

    @Test
    fun `setting a author filter reduces search results`() {
        val a = givenARecipe(category = "a", cuisine = "x", author = AuthorId("1")).also { recipeRepository.store(it) }
        val b = givenARecipe(category = "b", cuisine = "y", author = AuthorId("2")).also { recipeRepository.store(it) }
        givenARecipe(category = "c", cuisine = "z", author = AuthorId("3")).also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "",
                    limit = 10,
                    page = 0,
                    filter =
                        mapOf(
                            FilterKey.AUTHOR to listOf("1", "2"),
                        ),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactlyInAnyOrder(a, b)

        assertThat(result.possibleFilter).containsOnly(
            FilterKey.CATEGORY to listOf("a", "b"),
            FilterKey.CUISINE to listOf("x", "y"),
            FilterKey.AUTHOR to listOf("1", "2"),
        )
    }

    @Test
    fun `setting a filter and search query reduces search result`() {
        val recipe1 = givenARecipe(title = "recipe1", category = "a").also { recipeRepository.store(it) }
        val recipe2 = givenARecipe(title = "recipe2", category = "a").also { recipeRepository.store(it) }
        givenARecipe(title = "recipe3", category = "b").also { recipeRepository.store(it) }
        givenARecipe(title = "another", category = "a").also { recipeRepository.store(it) }

        val result =
            recipeRepository.search(
                SearchRequest(
                    query = "recipe",
                    limit = 10,
                    page = 0,
                    filter =
                        mapOf(
                            FilterKey.CATEGORY to listOf("a"),
                        ),
                    order = Order(field = OrderField.TITLE, direction = SortDir.DESC),
                ),
            )

        assertThat(result.results).containsExactlyInAnyOrder(recipe1, recipe2)
    }

    private fun givenARecipe(
        title: String = "title",
        category: String = "category",
        cuisine: String = "cuisine",
        author: AuthorId = testUser.toAuthor().id,
        createdAt: OffsetDateTime = OffsetDateTime.now(fixedClock),
    ) = Recipe(
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
        createdAt = createdAt,
    )
}
