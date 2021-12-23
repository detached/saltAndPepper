package de.w3is.recipes.infra.persistence

import de.w3is.recipes.application.users.InvitationRepository
import de.w3is.recipes.application.users.Invite
import de.w3is.recipes.application.users.UserId
import de.w3is.recipes.infra.persistence.generated.Tables.INVITATIONS
import jakarta.inject.Singleton
import org.jooq.DSLContext
import java.time.Duration
import java.time.OffsetDateTime

@Singleton
class JooqInvitationRepository(private val dslContext: DSLContext) : InvitationRepository {

    override fun store(invite: Invite) {
        dslContext.newRecord(INVITATIONS).apply {
            createdOn = invite.createdOn
            userId = invite.creator.value
            invitationCode = invite.code
        }.store()
    }

    override fun deleteAllOlderThan(duration: Duration) {
        dslContext.deleteFrom(INVITATIONS)
            .where(INVITATIONS.CREATED_ON.lessThan(OffsetDateTime.now().minus(duration)))
            .execute()
    }

    override fun findByCode(code: String): Invite? =
        dslContext.selectFrom(INVITATIONS)
            .where(INVITATIONS.INVITATION_CODE.eq(code))
            .fetchOne {
                Invite(
                    code = it.invitationCode,
                    createdOn = it.createdOn,
                    creator = UserId(it.userId)
                )
            }

    override fun invalidate(invite: Invite) {
        dslContext.deleteFrom(INVITATIONS).where(INVITATIONS.INVITATION_CODE.equal(invite.code)).execute()
    }
}