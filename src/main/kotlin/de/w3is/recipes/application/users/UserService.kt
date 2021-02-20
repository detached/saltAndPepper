package de.w3is.recipes.application.users

import java.lang.RuntimeException
import javax.inject.Singleton

@Singleton
class UserService(private val userRepository: UserRepository) {

    fun getUserFor(username: String): User =
        userRepository.findUser(username) ?: throw UserNotFoundException("User $username not found")
}

class UserNotFoundException(message: String): RuntimeException(message)