package de.w3is.recipes.domain

import de.w3is.recipes.domain.model.Recipe
import de.w3is.recipes.domain.model.RecipeId
import de.w3is.recipes.domain.model.SearchResponse

interface RecipeRepository {
    fun store(recipe: Recipe)
    fun get(recipeId: RecipeId): Recipe
    fun getAll(): List<Recipe>
    fun search(query: String, limit: Int, page: Int): SearchResponse
    fun delete(recipe: Recipe)
}