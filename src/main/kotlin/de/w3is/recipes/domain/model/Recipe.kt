package de.w3is.recipes.domain.model

import de.w3is.recipes.application.UpdateRecipeCommand
import java.util.*

class Recipe(
    val id: RecipeId,
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val ingredients: String,
    val instructions: String,
    val modifications: String,
    val authorId: AuthorId,
    images: List<ImageId>,
) {

    private val mutableImages: MutableList<ImageId> = images.toMutableList()

    fun getImages(): List<ImageId> = mutableImages
    fun addImage(imageId: ImageId, author: Author) {
        assertIsAuthoredBy(author)
        mutableImages.add(imageId)
    }
    fun removeImage(imageId: ImageId, author: Author) {
        assertIsAuthoredBy(author)
        mutableImages.remove(imageId)
    }

    fun updateWith(command: UpdateRecipeCommand, author: Author): Recipe {

        assertIsAuthoredBy(author)

        return Recipe(
            id = id,
            authorId = authorId,
            images = mutableImages,
            title = command.title,
            category = command.category,
            cuisine = command.cuisine,
            yields = command.yields,
            ingredients = command.ingredients,
            instructions = command.instructions,
            modifications = command.modifications,
        )
    }

    fun assertIsAuthoredBy(author: Author) {
        if (authorId != author.id) {
            throw NotAllowedToEditException()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Recipe

        if (id != other.id) return false
        if (title != other.title) return false
        if (category != other.category) return false
        if (cuisine != other.cuisine) return false
        if (yields != other.yields) return false
        if (ingredients != other.ingredients) return false
        if (instructions != other.instructions) return false
        if (modifications != other.modifications) return false
        if (authorId != other.authorId) return false
        if (mutableImages != other.mutableImages) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "Recipe(id=$id, title='$title')"
    }
}

data class RecipeId(val recipeId: String) {
    companion object {
        fun new(): RecipeId {
            return RecipeId(recipeId = UUID.randomUUID().toString())
        }
    }
}