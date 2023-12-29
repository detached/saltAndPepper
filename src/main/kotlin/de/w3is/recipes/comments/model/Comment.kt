package de.w3is.recipes.comments.model

import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.UserId
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@JvmInline
value class CommentId(
    val id: UUID,
) {
    companion object {
        fun new() = CommentId(UUID.randomUUID())
    }
}

data class Comment(
    val id: CommentId,
    val userId: UserId,
    val recipeId: RecipeId,
    val text: String,
    val createdAt: Instant,
) {
    companion object {

        context(Clock)
        fun createNew(userId: UserId, recipeId: RecipeId, text: String) = Comment(
            id = CommentId.new(),
            userId = userId,
            recipeId = recipeId,
            text = text,
            createdAt = Instant.now(this@Clock).truncatedTo(ChronoUnit.MILLIS),
        )
    }
}
