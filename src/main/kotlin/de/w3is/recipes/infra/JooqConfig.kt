package de.w3is.recipes.infra

import io.micronaut.context.annotation.Factory
import org.jooq.conf.RenderNameCase
import org.jooq.conf.Settings
import javax.inject.Named
import javax.inject.Singleton

@Factory
class JooqConfig {

    @Singleton
    @Named("postgres")
    fun postgresSettings() = Settings()
        .withRenderNameCase(RenderNameCase.LOWER)
        .withRenderSchema(false)
}