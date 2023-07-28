package de.w3is.recipes.recipes

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.infra.persistence.generated.tables.Images.Companion.IMAGES
import de.w3is.recipes.infra.persistence.generated.tables.Recipes.Companion.RECIPES
import de.w3is.recipes.recipes.model.Recipe
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
class RecipeServiceTest {

    @Inject
    private lateinit var dslContext: DSLContext

    @Inject
    private lateinit var recipeService: RecipeService

    @Inject
    private lateinit var imageRepository: ImageRepository

    @get:Bean
    @get:Replaces(Clock::class)
    val fixedClock: Clock = Clock.fixed(Instant.now(), ZoneOffset.UTC)

    @BeforeEach
    fun setUp() {
        dslContext.truncate(RECIPES).execute()
        dslContext.truncate(IMAGES).execute()
    }

    @Test
    fun `when storing new recipe it can be fetched`() {

        val newRecipe = recipeService.createNewRecipe(
            RecipeContent(
                title = "title",
                category = "category",
                cuisine = "cuisine",
                yields = "yields",
                ingredients = "ingredients",
                instructions = "instructions",
                modifications = "modifications",
                images = emptyList()
            ),
            user = testUser
        )

        val fetchedRecipe = recipeService.get(newRecipe.id)

        assertThat(newRecipe).isEqualTo(fetchedRecipe)
        assertThat(fetchedRecipe).isEqualTo(
            Recipe(
                id = newRecipe.id,
                title = "title",
                category = "category",
                cuisine = "cuisine",
                yields = "yields",
                ingredients = "ingredients",
                instructions = "instructions",
                modifications = "modifications",
                images = mutableListOf(),
                authorId = testUser.toAuthor().id,
                createdAt = OffsetDateTime.now(fixedClock)
            )
        )
    }

    @Test
    fun `when adding to recipe it can be fetched`() {

        val recipeId = recipeService.createNewRecipe(newRecipeContent(), testUser).id
        val imageData = givenImageData()

        val imageId = recipeService.addImageToRecipe(recipeId, imageData, testUser)
        val recipe = recipeService.get(recipeId)

        val imageIdFromRecipe = recipe.getImages()[0]

        assertThat(imageId).isEqualTo(imageIdFromRecipe)
        assertThat(imageRepository.get(imageId))
    }

    @Test
    fun `when removing image from recipe it is gone`() {

        val recipeId = recipeService.createNewRecipe(newRecipeContent(), testUser).id
        val imageData = givenImageData()

        val imageId = recipeService.addImageToRecipe(recipeId, imageData, testUser)

        assertThat(imageRepository.get(imageId))

        recipeService.deleteImageFromRecipe(recipeId, imageId, testUser)

        val recipe = recipeService.get(recipeId)

        assertThat(recipe.getImages()).isEmpty()
        assertFailure { imageRepository.get(imageId) }
    }

    private fun newRecipeContent() = RecipeContent(
        title = "title",
        category = "category",
        cuisine = "cuisine",
        yields = "yields",
        ingredients = "ingredients",
        instructions = "instructions",
        modifications = "modifications",
        images = emptyList()
    )

    private fun givenImageData() = this.javaClass.getResourceAsStream("/images/cake.jpg")!!
}