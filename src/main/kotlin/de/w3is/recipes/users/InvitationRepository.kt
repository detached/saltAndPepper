package de.w3is.recipes.users

import de.w3is.recipes.users.model.Invite
import de.w3is.recipes.users.model.UserId
import java.time.OffsetDateTime

interface InvitationRepository {
    fun store(invite: Invite)
    fun deleteAllOlderThan(dateTime: OffsetDateTime)
    fun findByCode(code: String): Invite?
    fun invalidate(invite: Invite)
    fun findByCreator(id: UserId): Invite?
}
