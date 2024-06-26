package de.w3is.recipes.users.infra.persistence

import de.w3is.recipes.infra.persistence.generated.tables.Invitations.Companion.INVITATIONS
import de.w3is.recipes.infra.persistence.generated.tables.records.InvitationsRecord
import de.w3is.recipes.users.InvitationRepository
import de.w3is.recipes.users.model.Invite
import de.w3is.recipes.users.model.UserId
import jakarta.inject.Singleton
import org.jooq.DSLContext
import java.time.Clock
import java.time.OffsetDateTime

@Singleton
class JooqInvitationRepository(
    private val clock: Clock,
    private val dslContext: DSLContext,
) : InvitationRepository {
    override fun store(invite: Invite) {
        dslContext.newRecord(INVITATIONS).apply {
            createdOn = invite.createdOn
            userId = invite.creator.value
            invitationCode = invite.code
        }.store()
    }

    override fun deleteAllOlderThan(dateTime: OffsetDateTime) {
        dslContext.deleteFrom(INVITATIONS)
            .where(INVITATIONS.CREATED_ON.lessThan(dateTime))
            .execute()
    }

    override fun findByCode(code: String): Invite? =
        dslContext.selectFrom(INVITATIONS)
            .where(INVITATIONS.INVITATION_CODE.eq(code))
            .fetchOne { it.toInvite() }

    override fun invalidate(invite: Invite) {
        dslContext.deleteFrom(INVITATIONS).where(INVITATIONS.INVITATION_CODE.equal(invite.code)).execute()
    }

    override fun findByCreator(id: UserId): Invite? =
        dslContext.selectFrom(INVITATIONS)
            .where(INVITATIONS.USER_ID.eq(id.value))
            .fetchOne { it.toInvite() }

    private fun InvitationsRecord.toInvite() =
        Invite(
            code = invitationCode!!,
            createdOn = createdOn!!,
            creator = UserId(userId!!),
        )
}
