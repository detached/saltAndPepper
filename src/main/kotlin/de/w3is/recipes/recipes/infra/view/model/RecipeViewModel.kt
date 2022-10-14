package de.w3is.recipes.recipes.infra.view.model

data class RecipeViewModel(
        val id: String,
        val title: String,
        val category: String,
        val cuisine: String,
        val yields: String,
        val ingredients: String,
        val instructions: String,
        val modifications: String,
        val author: String,
        val allowedToEdit: Boolean,
        val editUrl: String,
        val deleteUrl: String)