package de.w3is.recipes.comments.infra.persistence

import de.w3is.recipes.comments.CommentsRepository
import de.w3is.recipes.comments.model.Comment
import de.w3is.recipes.comments.model.CommentId
import de.w3is.recipes.common.toOffsetDateTime
import de.w3is.recipes.infra.persistence.generated.tables.Comments.Companion.COMMENTS
import de.w3is.recipes.infra.persistence.generated.tables.daos.CommentsDao
import de.w3is.recipes.infra.persistence.generated.tables.records.CommentsRecord
import de.w3is.recipes.recipes.model.RecipeId
import de.w3is.recipes.users.model.UserId
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.jooq.SortOrder
import java.time.Clock
import de.w3is.recipes.infra.persistence.generated.tables.pojos.Comments as CommentEntity

@Singleton
class JooqCommentsRepository(
    private val commentsDao: CommentsDao,
    private val dslContext: DSLContext,
    private val clock: Clock,
) : CommentsRepository {
    override fun store(comment: Comment) {
        val entity = with(clock) {
            CommentEntity(
                id = comment.id.id,
                userId = comment.userId.value,
                recipeId = comment.recipeId.recipeId,
                comment = comment.text,
                createdAt = comment.createdAt.toOffsetDateTime(),
            )
        }

        commentsDao.insert(entity)
    }

    override fun findAll(recipeId: RecipeId): List<Comment> =
        dslContext.selectFrom(COMMENTS)
            .where(COMMENTS.RECIPE_ID.eq(recipeId.recipeId))
            .orderBy(COMMENTS.CREATED_AT.sort(SortOrder.DESC))
            .map { it.toModel() }

    override fun getById(commentId: CommentId): Comment =
        commentsDao.findById(commentId.id)?.toModel() ?: error("No comment found for id $commentId")

    override fun delete(id: CommentId) {
        commentsDao.deleteById(id.id)
    }

    private fun CommentEntity.toModel() = Comment(
        id = CommentId(this.id!!),
        userId = UserId(this.userId!!),
        recipeId = RecipeId(this.recipeId!!),
        text = this.comment!!,
        createdAt = this.createdAt!!.toInstant(),
    )
}

private fun CommentsRecord.toModel() = Comment(
    id = CommentId(this.id!!),
    userId = UserId(this.userId!!),
    recipeId = RecipeId(this.recipeId!!),
    text = this.comment!!,
    createdAt = this.createdAt!!.toInstant(),
)
