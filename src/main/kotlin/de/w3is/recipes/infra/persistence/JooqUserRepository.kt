package de.w3is.recipes.infra.persistence

import de.w3is.recipes.application.users.*
import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.AuthorId
import de.w3is.recipes.infra.persistence.generated.Tables.*
import de.w3is.recipes.infra.persistence.generated.tables.records.UsersRecord
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
        id = UserId(it.userId),
        name = it.username,
        password = EncryptedPassword(it.password),
        role = Role.valueOf(it.role)
    )

    override fun store(user: User) {
        dslContext.newRecord(USERS).apply {
            userId = user.id.value
            username = user.name
            password = user.password.value
            role = user.role.name
        }.store()
    }

    override fun get(authorId: AuthorId): Author =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.equal(authorId.value)).fetchOne {
            Author(
                id = AuthorId(it.userId),
                name = it.username
            )
        } ?: error("Author with id $authorId")
}