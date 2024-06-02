package de.w3is.recipes.users

import de.w3is.recipes.users.infra.PasswordValidator
import de.w3is.recipes.users.model.EncryptedPassword
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.Role
import de.w3is.recipes.users.model.User
import de.w3is.recipes.users.model.UserId
import jakarta.inject.Singleton
import java.lang.RuntimeException

@Singleton
class UserService(
    private val userRepository: UserRepository,
    private val passwordValidator: PasswordValidator,
) {
    fun getUserFor(username: String): User = userRepository.findUser(username) ?: throw UserNotFoundException(username)

    fun getUser(userId: UserId): User = userRepository.getUser(userId)

    fun createNewUser(
        name: String,
        plainPassword: PlainPassword,
    ): User {
        validateUsername(name)
        validatePassword(name, plainPassword)

        return User.createNew(name, plainPassword, Role.USER).also {
            userRepository.store(it)
        }
    }

    fun changePassword(
        user: User,
        oldPassword: PlainPassword,
        newPassword: PlainPassword,
    ) {
        if (!user.authenticate(oldPassword)) {
            throw WrongPasswordException()
        }

        validatePassword(user.name, newPassword)

        user.copy(password = EncryptedPassword.fromPlain(newPassword)).also {
            userRepository.update(it)
        }
    }

    private fun validatePassword(
        name: String,
        plainPassword: PlainPassword,
    ) {
        if (!passwordValidator.isPasswordValid(name, plainPassword)) {
            throw PasswordTooWeekException()
        }
    }

    private fun validateUsername(name: String) {
        if (name.isBlank()) {
            throw UsernameInvalidException(name)
        }

        val existingUser = userRepository.findUser(name)
        if (existingUser != null) {
            throw UserAlreadyExistsException(existingUser.name)
        }
    }
}

class UserNotFoundException(username: String) : RuntimeException("User $username not found")

class UserAlreadyExistsException(username: String) : RuntimeException("User $username already exists")

class UsernameInvalidException(username: String) : RuntimeException("The username $username is invalid")

class PasswordTooWeekException : RuntimeException("The given password is to week")

class WrongPasswordException : RuntimeException("The provided password is not correct")
