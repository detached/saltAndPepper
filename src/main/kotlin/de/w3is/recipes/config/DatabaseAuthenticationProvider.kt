package de.w3is.recipes.config

import de.w3is.recipes.users.UserRepository
import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.AuthenticationFailureReason
import io.micronaut.security.authentication.AuthenticationProvider
import io.micronaut.security.authentication.AuthenticationRequest
import io.micronaut.security.authentication.AuthenticationResponse
import jakarta.inject.Singleton
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Singleton
class DatabaseAuthenticationProvider(private val userRepository: UserRepository) :
    AuthenticationProvider<HttpRequest<*>> {

    private val logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider::class.java)

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>,
    ): Publisher<AuthenticationResponse> = Mono.create { emitter ->

        val username = authenticationRequest.identity as String
        val plainPassword = PlainPassword(authenticationRequest.secret as String)

        val user = userRepository.findUser(username)

        if (user != null) {
            if (user.authenticate(plainPassword)) {
                emitter.success(
                    AuthenticationResponse.success(
                        user.id.value,
                        listOf(user.role.name),
                    ),
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
