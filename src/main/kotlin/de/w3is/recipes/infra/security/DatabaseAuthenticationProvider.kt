package de.w3is.recipes.infra.security

import de.w3is.recipes.application.users.UserRepository
import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import javax.inject.Singleton

@Singleton
class DatabaseAuthenticationProvider(private val userRepository: UserRepository) : AuthenticationProvider {

    private val logger = LoggerFactory.getLogger(DatabaseAuthenticationProvider::class.java)

    override fun authenticate(
        httpRequest: HttpRequest<*>?,
        authenticationRequest: AuthenticationRequest<*, *>
    ): Publisher<AuthenticationResponse> = Flowable.create({ emitter: FlowableEmitter<AuthenticationResponse> ->

        val username = authenticationRequest.identity as String
        val plainPassword = authenticationRequest.secret as String

        val user = userRepository.findUser(username)

        if (user != null) {
            if (user.authenticate(plainPassword)) {
                emitter.onNext(UserDetails(user.name, listOf(user.role.name), mapOf(Pair(userAttribute, user))))
                emitter.onComplete()
            } else {
                logger.warn("Login failed: Password incorrect for $username")
                emitter.onError(AuthenticationException(AuthenticationFailed(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)))
            }
        } else {
            logger.warn("User not found: $username")
            emitter.onError(AuthenticationException(AuthenticationFailed(AuthenticationFailureReason.USER_NOT_FOUND)))
        }

    }, BackpressureStrategy.ERROR)
}