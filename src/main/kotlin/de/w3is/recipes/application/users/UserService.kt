package de.w3is.recipes.application.users

import jakarta.inject.Singleton
import java.lang.RuntimeException

@Singleton
class UserService(private val userRepository: UserRepository) {

    fun getUserFor(username: String): User =
        userRepository.findUser(username) ?: throw UserNotFoundException("User $username not found")
}

class UserNotFoundException(message: String): RuntimeException(message)