package de.w3is.recipes.config

import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import java.time.Clock

@Factory
class ClockConfig {
    @Bean
    fun utcClock(): Clock = Clock.systemUTC()
}
