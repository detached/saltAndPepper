package de.w3is.recipes.config

import de.w3is.recipes.infra.persistence.generated.tables.references.REFRESH_TOKENS
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.jooq.DSLContext
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import java.time.Clock
import java.time.OffsetDateTime

@Singleton
class JooqRefreshTokenPersistence(
    private val dslContext: DSLContext,
    private val clock: Clock,
) : RefreshTokenPersistence {
    override fun persistToken(event: RefreshTokenGeneratedEvent?) {
        if (event?.refreshToken != null && event.authentication?.name != null) {
            dslContext.newRecord(REFRESH_TOKENS).apply {
                username = event.authentication.name
                refreshToken = event.refreshToken
                revoked = false
                createdAt = OffsetDateTime.now(clock)
            }.store()
        }
    }

    override fun getAuthentication(refreshToken: String?): Publisher<Authentication> =
        Flux.create({ emitter: FluxSink<Authentication> ->

            val token = dslContext.fetchOne(REFRESH_TOKENS.where(REFRESH_TOKENS.REFRESH_TOKEN.eq(refreshToken)))

            if (token != null) {
                if (token.revoked!!) {
                    emitter.error(
                        OauthErrorResponseException(
                            IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "refresh token revoked",
                            null,
                        ),
                    )
                } else {
                    emitter.next(Authentication.build(token.username!!))
                    emitter.complete()
                }
            } else {
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token not found",
                        null,
                    ),
                )
            }
        }, FluxSink.OverflowStrategy.ERROR)
}
