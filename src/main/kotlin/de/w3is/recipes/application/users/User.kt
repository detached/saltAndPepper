package de.w3is.recipes.application.users

import de.w3is.recipes.domain.model.Author
import de.w3is.recipes.domain.model.AuthorId
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

data class UserId(val value: String) {
    companion object {
        fun new(): UserId = UserId(UUID.randomUUID().toString())
    }
}

enum class Role {
    ADMIN,
    USER
}

data class User(
    val id: UserId,
    val name: String,
    val password: String,
    val role: Role
) {
    companion object {
        fun createNew(name: String, plainPassword: String, role: Role = Role.USER): User {
            return User(
                id = UserId.new(),
                name = name,
                password = BCryptPasswordEncoder().encode(plainPassword),
                role = role
            )
        }
    }

    fun authenticate(plainPassword: String) = BCryptPasswordEncoder().matches(plainPassword, password)

    override fun toString(): String {
        return "User(id=$id, name='$name')"
    }

    fun toAuthor() = Author(id = AuthorId(id.value), name = name)
}