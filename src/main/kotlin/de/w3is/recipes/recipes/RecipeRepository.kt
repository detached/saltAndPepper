package de.w3is.recipes.recipes

import de.w3is.recipes.recipes.model.Recipe
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.recipes.model.SearchRequest
import de.w3is.recipes.recipes.model.SearchResponse

interface RecipeRepository {
    fun store(recipe: Recipe)
    fun get(recipeId: RecipeId): Recipe
    fun getAll(): List<Recipe>
    fun search(searchRequest: SearchRequest): SearchResponse
    fun delete(recipe: Recipe)
}