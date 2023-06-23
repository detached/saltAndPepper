package de.w3is.recipes.importer

import de.w3is.recipes.images.ImageId
import de.w3is.recipes.images.ImageService
import de.w3is.recipes.recipes.model.Author
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.User
import de.w3is.recipes.recipes.RecipeRepository
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.Clock
import java.time.OffsetDateTime
import java.time.ZoneOffset

interface ImportCommandProvider : Iterator<ImportRecipe>

@Singleton
class ImportService(
    private val recipeRepository: RecipeRepository,
    private val imageService: ImageService,
    private val clock: Clock
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
            authorId = author.id,
            createdAt = OffsetDateTime.now(clock)
        )
    }
}