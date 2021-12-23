package de.w3is.recipes.application.users

import java.time.Duration

interface InvitationRepository {
    fun store(invite: Invite)
    fun deleteAllOlderThan(duration: Duration)
    fun findByCode(code: String): Invite?
    fun invalidate(invite: Invite)
}