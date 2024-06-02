package de.w3is.recipes.comments.infra.api

import de.w3is.recipes.comments.CommentsService
import de.w3is.recipes.comments.model.Comment
import de.w3is.recipes.comments.model.CommentId
import de.w3is.recipes.comments.model.CreateNewCommentCommand
import de.w3is.recipes.common.getUser
import de.w3is.recipes.common.toUserId
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.model.UserId
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import java.util.UUID

@Controller("/api/comments")
@Secured(SecurityRule.IS_AUTHENTICATED)
class CommentsController(
    private val commentsService: CommentsService,
    private val userService: UserService,
) {
    @Get("/recipe/{recipeId}")
    fun getAllCommentsOfRecipe(
        @PathVariable("recipeId") recipeId: String,
        authentication: Authentication,
    ): CommentsViewModel {
        val userId = authentication.toUserId()
        val allComments = commentsService.getAllComments(RecipeId(recipeId))

        return CommentsViewModel(
            recipeId = recipeId,
            comments =
                allComments.map { comment ->
                    comment.toVO(canDelete = comment.userId == userId)
                },
        )
    }

    @Post("/recipe/{recipeId}")
    fun createNewCommentOnRecipe(
        @PathVariable("recipeId") recipeId: String,
        @Body createCommentViewModel: CreateCommentViewModel,
        authentication: Authentication,
    ): CommentViewModel {
        val user = with(userService) { authentication.getUser() }
        val comment =
            commentsService.createNewCommentForRecipe(
                CreateNewCommentCommand(
                    userId = user.id,
                    recipeId = RecipeId(recipeId),
                    comment = createCommentViewModel.comment,
                ),
            )
        return comment.toVO(canDelete = true)
    }

    @Delete("/recipe/{recipeId}/comment/{commentId}")
    fun deleteCommentOnRecipe(
        @PathVariable("recipeId") recipeId: String,
        @PathVariable("commentId") commentId: UUID,
        authentication: Authentication,
    ) {
        val user = with(userService) { authentication.getUser() }
        commentsService.deleteComment(RecipeId(recipeId), CommentId(commentId), user.id)
    }

    private fun Comment.toVO(canDelete: Boolean) =
        CommentViewModel(
            id = id.id,
            author = userId.toAuthorViewModel(),
            comment = text,
            createdAt = createdAt,
            canDelete = canDelete,
        )

    private fun UserId.toAuthorViewModel(): AuthorViewModel {
        val user = userService.getUser(this)
        return AuthorViewModel(
            id = user.id.value,
            name = user.name,
        )
    }
}
