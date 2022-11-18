package de.w3is.recipes.users

import de.w3is.recipes.users.model.Invite
import de.w3is.recipes.users.model.UserId
import java.time.Duration

interface InvitationRepository {
    fun store(invite: Invite)
    fun deleteAllOlderThan(duration: Duration)
    fun findByCode(code: String): Invite?
    fun invalidate(invite: Invite)
    fun findByCreator(id: UserId): Invite?
}