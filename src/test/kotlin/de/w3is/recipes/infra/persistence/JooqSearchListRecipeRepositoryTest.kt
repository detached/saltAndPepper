package de.w3is.recipes.infra.persistence

import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEqualTo
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.infra.persistence.generated.public_.Tables.RECIPES
import de.w3is.recipes.testUser
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject

@MicronautTest
class JooqSearchListRecipeRepositoryTest {

    @Inject
    private lateinit var jooqRecipeRepository: JooqRecipeRepository

    @Inject
    private lateinit var dslContext: DSLContext

    @BeforeEach
    internal fun setUp() {
        dslContext.truncate(RECIPES).execute()
    }

    @Test
    fun storeAndGetRecipe() {

        val expected = givenARecipe()

        jooqRecipeRepository.store(expected)
        val actual = jooqRecipeRepository.get(expected.id)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    internal fun storeAndGetAll() {

        val expected = generateSequence { givenARecipe() }.take(5).toList()

        expected.forEach {
            jooqRecipeRepository.store(it)
        }

        val actual = jooqRecipeRepository.getAll().toList()

        assertThat(actual).containsOnly(*expected.toTypedArray())
    }

    private fun givenARecipe() = Recipe(
        id = RecipeId.new(),
        title = "titel",
        category = "category",
        cuisine = "cusine",
        yields = "yields",
        ingredients = "ingredients",
        instructions = "instructions",
        modifications = "modifications",
        images = mutableListOf(ImageId.new()),
        authorId = testUser.toAuthor().id
    )
}