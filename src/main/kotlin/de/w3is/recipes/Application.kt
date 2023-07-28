package de.w3is.recipes

import io.micronaut.runtime.Micronaut.build

fun main(args: Array<String>) {
    build()
        .args(*args)
        .packages("de.w3is.recipes")
        .start()
}
