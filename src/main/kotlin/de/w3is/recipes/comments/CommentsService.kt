package de.w3is.recipes.comments

import de.w3is.recipes.comments.model.Comment
import de.w3is.recipes.comments.model.CommentId
import de.w3is.recipes.comments.model.CreateNewCommentCommand
import de.w3is.recipes.recipes.RecipeService
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.UserId
import jakarta.inject.Singleton
import java.time.Clock

@Singleton
class CommentsService(
    private val recipeService: RecipeService,
    private val commentsRepository: CommentsRepository,
    private val clock: Clock,
) {

    fun createNewCommentForRecipe(command: CreateNewCommentCommand): Comment {
        check(recipeService.exists(command.recipeId)) {
            "Recipe with id ${command.recipeId} has to exist to create comment for it"
        }

        val comment = with(clock) {
            Comment.createNew(
                userId = command.userId,
                recipeId = command.recipeId,
                text = command.comment,
            )
        }

        commentsRepository.store(comment)

        return comment
    }

    fun getAllComments(recipeId: RecipeId): List<Comment> = commentsRepository.findAll(recipeId)

    fun deleteComment(recipeId: RecipeId, commentId: CommentId, userId: UserId) {
        val comment = commentsRepository.getById(commentId)

        check(comment.recipeId == recipeId) {
            "Comment was created for different recipe"
        }
        check(comment.userId == userId) {
            "Comment was created by different user"
        }

        commentsRepository.delete(comment.id)
    }
}
