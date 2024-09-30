package de.w3is.recipes.users.infra.persistence

import de.w3is.recipes.common.model.Page
import de.w3is.recipes.common.model.PageRequest
import de.w3is.recipes.common.model.PageResponse
import de.w3is.recipes.infra.persistence.generated.tables.Users.Companion.USERS
import de.w3is.recipes.infra.persistence.generated.tables.daos.UsersDao
import de.w3is.recipes.infra.persistence.generated.tables.pojos.Users
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
import kotlin.math.ceil

@Singleton
open class JooqUserRepository(
    private val usersDao: UsersDao,
    private val dslContext: DSLContext,
) : UserRepository, AuthorRepository {
    override fun findUser(userName: String): User? = usersDao.fetchOneByUsername(userName)?.toUser()

    override fun getUser(userId: UserId): User = usersDao.fetchOneByUserId(userId.value)?.toUser() ?: error("User for id $userId not found")

    override fun get(authorId: AuthorId): Author {
        usersDao.fetchByUserId(authorId.value)
        return dslContext.selectFrom(USERS).where(USERS.USER_ID.equal(authorId.value))
            .fetchOne { recordToAuthor(it) }
            ?: error("Author with id $authorId")
    }

    override fun get(authorIds: Set<AuthorId>): Set<Author> =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.`in`(authorIds.map { it.value }))
            .fetch { recordToAuthor(it) }.toSet()

    override fun getAllUsers(request: PageRequest): PageResponse<User> =
        dslContext.selectFrom(USERS)
            .limit(request.limit)
            .offset(request.page * request.limit)
            .fetch { it.toUser() }.let { users ->
                PageResponse(
                    results = users,
                    page =
                        Page(
                            current = request.page,
                            max = ceil(usersDao.count().toDouble() / request.limit).toInt() - 1,
                            size = users.size,
                        ),
                )
            }

    override fun store(user: User) {
        usersDao.insert(
            Users(
                userId = user.id.value,
                username = user.name,
                password = user.password.value,
                role = user.role.name,
            ),
        )
    }

    override fun update(user: User) {
        dslContext.update(USERS)
            .set(USERS.PASSWORD, user.password.value)
            .set(USERS.ROLE, user.role.name)
            .where(USERS.USER_ID.equal(user.id.value))
            .execute()
    }

    private fun recordToAuthor(it: UsersRecord) =
        Author(
            id = AuthorId(it.userId!!),
            name = it.username!!,
        )

    private fun Users.toUser() =
        User(
            id = UserId(userId!!),
            name = username!!,
            password = EncryptedPassword(password!!),
            role = Role.valueOf(role!!),
        )

    private fun UsersRecord.toUser() =
        User(
            id = UserId(userId!!),
            name = username!!,
            password = EncryptedPassword(password!!),
            role = Role.valueOf(role!!),
        )
}
