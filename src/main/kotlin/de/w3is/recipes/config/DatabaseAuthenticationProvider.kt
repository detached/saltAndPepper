package de.w3is.recipes.config

import de.w3is.recipes.users.UserRepository
import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class DatabaseAuthenticationProvider<B>(private val userRepository: UserRepository) :
    HttpRequestAuthenticationProvider<B> {
    private val logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider::class.java)

    override fun authenticate(
        requestContext: HttpRequest<B>,
        authRequest: AuthenticationRequest<String, String>,
    ): AuthenticationResponse {
        val username = authRequest.identity as String
        val plainPassword = PlainPassword(authRequest.secret as String)

        val user = userRepository.findUser(username)

        if (user != null) {
            if (user.authenticate(plainPassword)) {
                return AuthenticationResponse.success(
                    user.id.value,
                    listOf(user.role.name),
                )
            } else {
                logger.warn("Login failed: Password incorrect for $username")
                throw AuthenticationResponse.exception(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
            }
        } else {
            logger.warn("User not found: $username")
            throw AuthenticationResponse.exception(AuthenticationFailureReason.USER_NOT_FOUND)
        }
    }
}
