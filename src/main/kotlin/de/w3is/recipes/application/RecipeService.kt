package de.w3is.recipes.application

import de.w3is.recipes.application.users.User
import de.w3is.recipes.domain.RecipeRepository
import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.ImageId
import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import java.io.InputStream
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val imageService: ImageService
) {

    fun getAll() = recipeRepository.getAll()

    fun get(recipeId: RecipeId) = recipeRepository.get(recipeId)

    @Transactional
    fun createNewRecipe(command: NewRecipeCommand, user: User): Recipe {

        val author = user.toAuthor()
        val recipe = command.toRecipe(author)
        recipeRepository.store(recipe)
        return recipe
    }

    @Transactional
    fun updateRecipe(updateRecipeCommand: UpdateRecipeCommand, user: User): Recipe {

        val author = user.toAuthor()
        val recipe = recipeRepository.get(updateRecipeCommand.id)
        val updatedRecipe = recipe.updateWith(updateRecipeCommand, author)
        recipeRepository.store(updatedRecipe)
        return updatedRecipe
    }

    @Transactional
    fun addImageToRecipe(recipeId: RecipeId, imageData: InputStream, user: User): ImageId {

        val recipe = recipeRepository.get(recipeId)
        val newImage = imageService.convertAndStoreImage(imageData)
        recipe.addImage(newImage, user.toAuthor())
        recipeRepository.store(recipe)
        return newImage
    }

    @Transactional
    fun deleteImageFromRecipe(recipeId: RecipeId, imageId: ImageId, user: User) {

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

data class NewRecipeCommand(
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String
) {
    fun toRecipe(author: Author): Recipe {
        return Recipe(
            id = RecipeId.new(),
            title = title,
            category = category,
            cuisine = cuisine,
            yields = yields,
            ingredients = ingredients,
            instructions = instructions,
            modifications = modifications,
            images = mutableListOf(),
            authorId = author.id
        )
    }
}

data class UpdateRecipeCommand(
    val id: RecipeId,
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String
)
