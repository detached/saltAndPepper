package de.w3is.recipes.application.importer

import de.w3is.recipes.application.ImageService
import de.w3is.recipes.application.users.User
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

interface ImportCommandProvider : Iterator<ImportRecipe>

@Singleton
class ImportService(
    private val recipeRepository: RecipeRepository,
    private val imageService: ImageService
) {

    private val logger = LoggerFactory.getLogger(ImportService::class.java)

    fun import(provider: ImportCommandProvider, user: User) {

        val author = user.toAuthor()

        provider.forEachRemaining {

            logger.info("Import ${it.title}")

            val imageId = if (it.image != null) {
                imageService.convertAndStoreImage(it.image)
            } else {
                null
            }

            val recipe = it.toNewRecipe(author, imageId)
            recipeRepository.store(recipe)
        }
    }
}

private fun ImportRecipe.toNewRecipe(author: Author, imageId: ImageId?): Recipe {

    return Recipe(
        id = RecipeId.new(),
        title = title,
        category = category,
        cuisine = cuisine,
        yields = yields,
        ingredients = ingredients,
        instructions = instructions,
        modifications = modifications,
        images = if (imageId == null) {
            mutableListOf()
        } else {
            mutableListOf(imageId)
        },
        authorId = author.id
    )
}
