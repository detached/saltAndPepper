package de.w3is.recipes.infra.importer

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import de.w3is.recipes.application.importer.ImportRecipe
import org.junit.jupiter.api.Test

class GourmetSearchListRecipeSourceTest {

    @Test
    fun `importing single recipe`() {

        val singleXml = this.javaClass.classLoader.getResourceAsStream("gourmet/single.xml")!!
        val expected = givenSingleRecipe()

        val recipeSource = GourmetRecipeSource(singleXml)

        assertThat(recipeSource.hasNext()).isTrue()
        assertThat(recipeSource.next()).all {
            isEqualToIgnoringGivenProperties(expected, ImportRecipe::image)
            transform { it.image }.isNotNull()
        }
        assertThat(recipeSource.hasNext()).isFalse()
    }

    @Test
    fun `import whole recipe collection`() {

        val collection = this.javaClass.classLoader.getResourceAsStream("gourmet/Rezepte.xml")!!

        val recipeSource = GourmetRecipeSource(collection)

        assertThat{
            recipeSource.forEachRemaining {  }
        }.isSuccess()
    }

    private fun givenSingleRecipe() = ImportRecipe(
            title = "This is a title",
            category = "A category",
            cuisine = "german",
            yields = "4 meals",
            image = null,
            ingredients = "A ingredient",
            instructions = """
                This is
                a
                multiline
                instruction.
            """.trimIndent(),
            modifications = "Maybe do it different!"
    )
}