package de.w3is.recipes.comments

import assertk.assertFailure
import de.w3is.recipes.comments.model.Comment
import de.w3is.recipes.comments.model.CommentId
import de.w3is.recipes.comments.model.CreateNewCommentCommand
import de.w3is.recipes.recipes.RecipeService
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.UserId
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import java.time.Clock
import java.time.Instant
import java.time.ZoneId

class CommentsServiceTest {
    private val recipeService = mock<RecipeService>()
    private val commentsRepository = mock<CommentsRepository>()
    private val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    private val commentsService =
        CommentsService(
            recipeService = recipeService,
            commentsRepository = commentsRepository,
            clock = clock,
        )

    @Test
    fun `given no recipe when try to create comment then it fail`() {
        val userId = UserId("test")
        val recipeId = RecipeId("doesn't exist")

        whenever(recipeService.exists(recipeId)).thenReturn(false)

        assertFailure {
            commentsService.createNewCommentForRecipe(
                CreateNewCommentCommand(
                    userId = userId,
                    recipeId = recipeId,
                    comment = "comment",
                ),
            )
        }

        verifyNoInteractions(commentsRepository)
    }

    @Test
    fun `given recipe when create new comment then comment is stored`() {
        val userId = UserId("test")
        val recipeId = RecipeId("recipe")

        whenever(recipeService.exists(recipeId)).thenReturn(true)

        val comment =
            commentsService.createNewCommentForRecipe(
                CreateNewCommentCommand(
                    userId = userId,
                    recipeId = recipeId,
                    comment = "comment",
                ),
            )

        verify(commentsRepository).store(comment)
    }

    @Test
    fun `given comment for other recipe when try to delete it with wrong recipeId then it fail`() {
        val userId = UserId("test")
        val wrongRecipeId = RecipeId("doesn't exist")
        val comment = givenComment(userId, RecipeId("recipeId"))

        whenever(commentsRepository.getById(comment.id)).thenReturn(comment)

        assertFailure {
            commentsService.deleteComment(wrongRecipeId, comment.id, userId)
        }

        verify(commentsRepository, never()).delete(comment.id)
    }

    @Test
    fun `given comment for other user when try to delete it with wrong userId then it fail`() {
        val wrongUserId = UserId("the other")
        val recipeId = RecipeId("recipeId")
        val comment = givenComment(UserId("test"), recipeId)

        whenever(commentsRepository.getById(comment.id)).thenReturn(comment)

        assertFailure {
            commentsService.deleteComment(recipeId, comment.id, wrongUserId)
        }

        verify(commentsRepository, never()).delete(comment.id)
    }

    @Test
    fun `given comment when delete it then remove it from repository`() {
        val userId = UserId("the other")
        val recipeId = RecipeId("recipeId")
        val comment = givenComment(userId, recipeId)

        whenever(commentsRepository.getById(comment.id)).thenReturn(comment)

        commentsService.deleteComment(recipeId, comment.id, userId)

        verify(commentsRepository).delete(comment.id)
    }

    private fun givenComment(
        userId: UserId,
        recipeId: RecipeId,
    ): Comment =
        Comment(
            id = CommentId.new(),
            userId = userId,
            recipeId = recipeId,
            text = "",
            createdAt = Instant.now(clock),
        )
}
