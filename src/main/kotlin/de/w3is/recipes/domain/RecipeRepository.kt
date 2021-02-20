package de.w3is.recipes.domain

import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId

interface RecipeRepository {
    fun store(recipe: Recipe)
    fun get(recipeId: RecipeId): Recipe
    fun getAll(): Sequence<Recipe>
    fun search(queryString: String, maxResults: Int): Sequence<Recipe>
}