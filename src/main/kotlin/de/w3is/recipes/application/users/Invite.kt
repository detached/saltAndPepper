package de.w3is.recipes.application.users

import java.time.OffsetDateTime
import java.util.*

data class Invite(
    val code: String,
    val createdOn: OffsetDateTime,
    val creator: UserId,
) {

    companion object {

        fun createNew(creator: User): Invite = Invite(
            code = UUID.randomUUID().toString(),
            createdOn = OffsetDateTime.now(),
            creator = creator.id
        )
    }
}

