package de.w3is.recipes.recipes

import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.recipes.model.SearchResponse

interface RecipeRepository {
    fun store(recipe: Recipe)
    fun get(recipeId: RecipeId): Recipe
    fun getAll(): List<Recipe>
    fun search(query: String, limit: Int, page: Int): SearchResponse
    fun delete(recipe: Recipe)
}