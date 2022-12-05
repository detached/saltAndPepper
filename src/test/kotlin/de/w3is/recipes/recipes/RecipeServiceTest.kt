package de.w3is.recipes.recipes

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isSuccess
import de.w3is.recipes.images.ImageRepository
import de.w3is.recipes.infra.persistence.generated.Tables
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.testUser
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class RecipeServiceTest {

    @Inject
    private lateinit var dslContext: DSLContext

    @Inject
    private lateinit var recipeService: RecipeService

    @Inject
    private lateinit var imageRepository: ImageRepository

    @BeforeEach
    fun setUp() {
        dslContext.truncate(Tables.RECIPES).execute()
        dslContext.truncate(Tables.IMAGES).execute()
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
                authorId = testUser.toAuthor().id
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
        assertThat { imageRepository.get(imageId) }.isSuccess()
    }

    @Test
    fun `when removing image from recipe it is gone`() {

        val recipeId = recipeService.createNewRecipe(newRecipeContent(), testUser).id
        val imageData = givenImageData()

        val imageId = recipeService.addImageToRecipe(recipeId, imageData, testUser)

        assertThat { imageRepository.get(imageId) }.isSuccess()

        recipeService.deleteImageFromRecipe(recipeId, imageId, testUser)

        val recipe = recipeService.get(recipeId)

        assertThat(recipe.getImages()).isEmpty()
        assertThat { imageRepository.get(imageId) }.isFailure()
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