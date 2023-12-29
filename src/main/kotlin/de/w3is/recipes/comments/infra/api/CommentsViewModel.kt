package de.w3is.recipes.comments.infra.api

import io.micronaut.serde.annotation.Serdeable
import java.time.Instant
import java.util.UUID

@Serdeable
data class CommentsViewModel(
    val recipeId: String,
    val comments: List<CommentViewModel>,
)

@Serdeable
data class CommentViewModel(
    val id: UUID,
    val author: AuthorViewModel,
    val comment: String,
    val createdAt: Instant,
    val canDelete: Boolean,
)

@Serdeable
data class AuthorViewModel(
    val id: String,
    val name: String,
)

@Serdeable
data class CreateCommentViewModel(
    val comment: String,
)
