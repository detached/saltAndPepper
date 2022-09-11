package de.w3is.recipes.config

import de.w3is.recipes.common.userAttribute
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.UserRepository
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Singleton
class DatabaseAuthenticationProvider(private val userRepository: UserRepository) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider::class.java)

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> = Mono.create { emitter ->

        val username = authenticationRequest.identity as String
        val plainPassword = PlainPassword(authenticationRequest.secret as String)

        val user = userRepository.findUser(username)

        if (user != null) {
            if (user.authenticate(plainPassword)) {
                emitter.success(
                    AuthenticationResponse.success(
                        user.name,
                        listOf(user.role.name),
                        mapOf(userAttribute to user)
                    )
                )
            } else {
                logger.warn("Login failed: Password incorrect for $username")
                emitter.error(AuthenticationResponse.exception(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH))
            }
        } else {
            logger.warn("User not found: $username")
            emitter.error(AuthenticationResponse.exception(AuthenticationFailureReason.USER_NOT_FOUND))
        }
    }
}