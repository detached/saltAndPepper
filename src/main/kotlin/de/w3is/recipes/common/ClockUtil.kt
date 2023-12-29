package de.w3is.recipes.common

import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime

context(Clock)
fun Instant.toOffsetDateTime(): OffsetDateTime {
    val offset = this@Clock.zone.rules.getOffset(this)
    return this.atOffset(offset)
}
