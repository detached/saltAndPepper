package de.w3is.recipes.users.model

import de.w3is.recipes.recipes.model.Author
import de.w3is.recipes.recipes.model.AuthorId
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

data class PlainPassword(val value: String) {
    override fun toString() = "xxx"
}

data class EncryptedPassword(val value: String) {
    companion object {
        fun fromPlain(plainPassword: PlainPassword): EncryptedPassword {
            return EncryptedPassword(BCryptPasswordEncoder().encode(plainPassword.value))
        }
    }
}

data class User(
    val id: UserId,
    val name: String,
    val password: EncryptedPassword,
    val role: Role
) {
    companion object {
        fun createNew(name: String, password: PlainPassword, role: Role = Role.USER): User {
            return User(
                id = UserId.new(),
                name = name,
                password = EncryptedPassword.fromPlain(password),
                role = role
            )
        }
    }

    fun authenticate(plainPassword: PlainPassword) =
        BCryptPasswordEncoder().matches(plainPassword.value, password.value)

    override fun toString(): String {
        return "User(id=$id, name='$name')"
    }

    fun toAuthor() = Author(id = AuthorId(id.value), name = name)
}