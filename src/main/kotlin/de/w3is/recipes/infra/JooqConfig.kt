package de.w3is.recipes.infra

import io.micronaut.context.annotation.Factory
import jakarta.inject.Named
import jakarta.inject.Singleton
import org.jooq.conf.RenderNameCase
import org.jooq.conf.Settings

@Factory
class JooqConfig {

    @Singleton
    @Named("postgres")
    fun postgresSettings() = Settings()
        .withRenderNameCase(RenderNameCase.LOWER)
        .withRenderSchema(false)

    @Singleton
    @Named("h2")
    fun h2Settings() = Settings()
        .withRenderNameCase(RenderNameCase.UPPER)
        .withRenderSchema(false)

}