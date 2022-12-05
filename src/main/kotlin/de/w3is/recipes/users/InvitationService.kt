package de.w3is.recipes.users

import de.w3is.recipes.users.model.Invite
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.User
import io.micronaut.context.annotation.Property
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.lang.RuntimeException
import java.time.Duration
import javax.transaction.Transactional

@Singleton
open class InvitationService(
    private val invitationRepository: InvitationRepository,
    private val userService: UserService,
    @Property(name = "application.allowInvitationFor")
    private val allowInvitationForRole: List<String>,
) {

    private val logger = LoggerFactory.getLogger(InvitationService::class.java)

    fun findExistingInvite(creator: User): Invite? {
        if(!isAllowedToInvite(creator)) {
            return null
        }

        return invitationRepository.findByCreator(creator.id)
    }

    @Transactional
    open fun createInvite(creator: User): Invite {

        if (!isAllowedToInvite(creator)) {
            throw NotAllowedToInviteException()
        }

        assert(invitationRepository.findByCreator(creator.id) == null) {
            "User has already created an invite code"
        }

        return Invite.createNew(creator).also {
            invitationRepository.store(it)
            logger.info("${creator.name} created invite ${it.code}")
        }
    }

    @Scheduled(fixedDelay = "1h")
    fun deleteOldInvitations() {
        logger.debug("Delete old invites")
        invitationRepository.deleteAllOlderThan(Duration.ofDays(1))
    }

    fun isAllowedToInvite(user: User): Boolean {
        return user.role.name in allowInvitationForRole
    }

    fun getInviteByCode(code: String): Invite = invitationRepository.findByCode(code) ?: throw InvitationNotFoundException()

    @Transactional
    open fun createUserByInvite(code: String, name: String, plainPassword: PlainPassword): User {

        val invite = getInviteByCode(code)
        val user = userService.createNewUser(name, plainPassword)
        invitationRepository.invalidate(invite)
        logger.info("Created user ${user.name} by invite ${invite.code}")
        return user
    }
}

class NotAllowedToInviteException : RuntimeException()
class InvitationNotFoundException : RuntimeException()