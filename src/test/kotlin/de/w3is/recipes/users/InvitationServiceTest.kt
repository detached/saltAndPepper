package de.w3is.recipes.users

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEmpty
import de.w3is.recipes.infra.persistence.generated.tables.Invitations.Companion.INVITATIONS
import de.w3is.recipes.testUser
import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
@Transactional
open class InvitationServiceTest {
    @Inject
    private lateinit var invitationService: InvitationService

    @Inject
    private lateinit var userService: UserService

    @Inject
    private lateinit var dslContext: DSLContext

    @BeforeEach
    fun reset() {
        dslContext.truncate(INVITATIONS).execute()
    }

    @Test
    fun `when creating a invite then the creating user is recorded as creator`() {
        val invite = invitationService.createInvite(testUser)

        assertThat(invite.creator).isEqualTo(testUser.id)
        assertThat(invite.code).isNotEmpty()
    }

    @Test
    fun `when a user has created a invitation then she can't create another one`() {
        assertFailure {
            invitationService.createInvite(testUser)
            invitationService.createInvite(testUser)
        }
    }

    @Test
    fun `when a person is invited than a user can be created`() {
        val userName = "newUser"

        val invite = invitationService.createInvite(testUser)

        invitationService.createUserByInvite(invite.code, userName, PlainPassword("password"))

        assertThat(userService.getUserFor(userName))
    }

    @Test
    fun `when a invite was used it can't be used again`() {
        val invite = invitationService.createInvite(testUser)

        invitationService.createUserByInvite(invite.code, "a", PlainPassword("password"))
        assertFailure {
            invitationService.createUserByInvite(invite.code, "b", PlainPassword("password"))
        }
    }

    @Test
    fun `can't create user with non existing invite code`() {
        assertFailure {
            invitationService.createUserByInvite("xyz", "a", PlainPassword("password"))
        }
    }
}
