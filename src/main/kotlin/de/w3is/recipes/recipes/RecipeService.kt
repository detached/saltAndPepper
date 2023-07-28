package de.w3is.recipes.recipes

import de.w3is.recipes.images.ImageService
import de.w3is.recipes.images.model.ImageId
import de.w3is.recipes.recipes.model.Author
import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.User
import jakarta.inject.Singleton
import java.io.InputStream
import java.time.Clock
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Singleton
open class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val imageService: ImageService,
    private val clock: Clock,
) {

    fun getAll() = recipeRepository.getAll()

    fun get(recipeId: RecipeId) = recipeRepository.get(recipeId)

    @Transactional
    open fun createNewRecipe(content: RecipeContent, user: User): Recipe {
        val author = user.toAuthor()
        val recipe = content.toNewRecipe(author, createdAt = OffsetDateTime.now(clock))
        recipeRepository.store(recipe)
        return recipe
    }

    @Transactional
    open fun updateRecipe(id: RecipeId, content: RecipeContent, user: User): Recipe {
        val author = user.toAuthor()
        val recipe = recipeRepository.get(id)
        val updatedRecipe = recipe.updateWith(content, author)
        recipeRepository.store(updatedRecipe)
        return updatedRecipe
    }

    @Transactional
    open fun addImageToRecipe(recipeId: RecipeId, imageData: InputStream, user: User): ImageId {
        val recipe = recipeRepository.get(recipeId)
        val newImage = imageService.convertAndStoreImage(imageData)
        recipe.addImage(newImage, user.toAuthor())
        recipeRepository.store(recipe)
        return newImage
    }

    @Transactional
    open fun deleteImageFromRecipe(recipeId: RecipeId, imageId: ImageId, user: User) {
        val recipe = recipeRepository.get(recipeId)
        recipe.removeImage(imageId, user.toAuthor())
        recipeRepository.store(recipe)
        imageService.delete(imageId)
    }

    fun deleteRecipe(recipeId: RecipeId, user: User) {
        val author = user.toAuthor()
        val recipe = recipeRepository.get(recipeId)
        recipe.assertIsAuthoredBy(author)
        recipeRepository.delete(recipe)
        recipe.getImages().forEach {
            imageService.delete(it)
        }
    }
}

data class RecipeContent(
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String,
    val images: List<ImageId>,
) {
    fun toNewRecipe(author: Author, createdAt: OffsetDateTime) = Recipe(
        id = RecipeId.new(),
        title = title,
        category = category,
        cuisine = cuisine,
        yields = yields,
        ingredients = ingredients,
        instructions = instructions,
        modifications = modifications,
        images = mutableListOf(),
        authorId = author.id,
        createdAt = createdAt,
    )
}
