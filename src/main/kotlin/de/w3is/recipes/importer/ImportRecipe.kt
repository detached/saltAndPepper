package de.w3is.recipes.importer

import java.io.InputStream

data class ImportRecipe(
    val title: String,
    val category: String,
    val cuisine: String,
    val yields: String,
    val image: InputStream?,
    val ingredients: String,
    val instructions: String,
    val modifications: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImportRecipe

        if (title != other.title) return false
        if (category != other.category) return false
        if (cuisine != other.cuisine) return false
        if (yields != other.yields) return false
        if (ingredients != other.ingredients) return false
        if (instructions != other.instructions) return false
        if (modifications != other.modifications) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + cuisine.hashCode()
        result = 31 * result + yields.hashCode()
        result = 31 * result + ingredients.hashCode()
        result = 31 * result + instructions.hashCode()
        result = 31 * result + modifications.hashCode()
        return result
    }
}
