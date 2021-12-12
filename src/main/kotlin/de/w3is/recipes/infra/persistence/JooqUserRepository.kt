package de.w3is.recipes.infra.persistence

import de.w3is.recipes.application.users.Role
import de.w3is.recipes.application.users.User
import de.w3is.recipes.application.users.UserId
import de.w3is.recipes.application.users.UserRepository
import de.w3is.recipes.domain.AuthorRepository
import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.AuthorId
import de.w3is.recipes.infra.persistence.generated.Tables.*
import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.Cacheable
import jakarta.inject.Singleton
import org.jooq.DSLContext

@Singleton
@CacheConfig(cacheNames = ["users"])
open class JooqUserRepository(private val dslContext: DSLContext) : UserRepository, AuthorRepository {

    @Cacheable
    override fun findUser(userName: String): User? =
        dslContext.selectFrom(USERS).where(USERS.USERNAME.equal(userName)).fetchOne {
            User(
                id = UserId(it.userId),
                name = it.username,
                password = it.password,
                role = Role.valueOf(it.role)
            )
        }

    override fun store(user: User) {
        dslContext.newRecord(USERS).apply {
            userId = user.id.value
            username = user.name
            password = user.password
            role = user.role.name
        }.store()
    }

    @Cacheable
    override fun get(authorId: AuthorId): Author =
        dslContext.selectFrom(USERS).where(USERS.USER_ID.equal(authorId.value)).fetchOne {
            Author(
                id = AuthorId(it.userId),
                name = it.username
            )
        } ?: error("Author with id $authorId")
}