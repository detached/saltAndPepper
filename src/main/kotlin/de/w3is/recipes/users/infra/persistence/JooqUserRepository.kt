package de.w3is.recipes.users.infra.persistence

import de.w3is.recipes.infra.persistence.generated.tables.Users.Companion.USERS
import de.w3is.recipes.infra.persistence.generated.tables.records.UsersRecord
import de.w3is.recipes.recipes.AuthorRepository
import de.w3is.recipes.recipes.model.Author
import de.w3is.recipes.recipes.model.AuthorId
import de.w3is.recipes.users.UserRepository
import de.w3is.recipes.users.model.EncryptedPassword
import de.w3is.recipes.users.model.Role
import de.w3is.recipes.users.model.User
import de.w3is.recipes.users.model.UserId
import jakarta.inject.Singleton
import org.jooq.DSLContext

@Singleton
open class JooqUserRepository(private val dslContext: DSLContext) : UserRepository, AuthorRepository {

    override fun findUser(userName: String): User? =
        dslContext.selectFrom(USERS).where(USERS.USERNAME.equal(userName))
            .fetchOne { toUser(it) }

    override fun getUser(userId: UserId): User =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.equal(userId.value))
            .fetchOne { toUser(it) } ?: error("User for id $userId not found")

    private fun toUser(it: UsersRecord) = User(
        id = UserId(it.userId!!),
        name = it.username!!,
        password = EncryptedPassword(it.password!!),
        role = Role.valueOf(it.role!!),
    )

    override fun store(user: User) {
        dslContext.newRecord(USERS).apply {
            userId = user.id.value
            username = user.name
            password = user.password.value
            role = user.role.name
        }.store()
    }

    override fun update(user: User) {
        dslContext.update(USERS)
            .set(USERS.PASSWORD, user.password.value)
            .set(USERS.ROLE, user.role.name)
            .where(USERS.USER_ID.equal(user.id.value))
            .execute()
    }

    override fun get(authorId: AuthorId): Author =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.equal(authorId.value))
            .fetchOne { recordToAuthor(it) }
            ?: error("Author with id $authorId")

    override fun get(authorIds: Set<AuthorId>): Set<Author> =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.`in`(authorIds.map { it.value }))
            .fetch { recordToAuthor(it) }.toSet()

    private fun recordToAuthor(it: UsersRecord) = Author(
        id = AuthorId(it.userId!!),
        name = it.username!!,
    )
}
