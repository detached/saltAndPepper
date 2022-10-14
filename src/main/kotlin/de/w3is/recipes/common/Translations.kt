package de.w3is.recipes.common

import java.util.*

class Translations(localesToLoad: Collection<Locale>, default: Locale) {

    private val locales: Map<Locale, Properties>
    private var activeLocale: Locale = default

    init {
        locales = mutableMapOf()

        localesToLoad.forEach {
            val properties = loadProperties(it)
            locales[it] = properties
        }
    }

    fun get(key: String): String = locales[activeLocale]?.get(key).toString()

    companion object {
        fun loadProperties(locale: Locale) = Properties().apply {

            val file = "/translations/${locale}.properties"
            val resource = Translations::class.java.getResourceAsStream(file)
                    ?: error("Didn't find $file")
            load(resource)
        }
    }
}