package de.w3is.recipes.infra.views.model

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import java.util.*

@Factory
class TranslationsFactory {

    @Singleton
    fun loadTranslations(): Translations = Translations(
            localesToLoad = listOf(Locale.GERMANY),
            default = Locale.GERMANY)
}