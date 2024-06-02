package de.w3is.recipes.comments

import de.w3is.recipes.comments.model.Comment
import de.w3is.recipes.comments.model.CommentId
import de.w3is.recipes.recipes.model.RecipeId

interface CommentsRepository {
    fun store(comment: Comment)

    fun findAll(recipeId: RecipeId): List<Comment>

    fun getById(commentId: CommentId): Comment

    fun delete(id: CommentId)
}
