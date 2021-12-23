package de.w3is.recipes.application.users

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import assertk.assertions.isNotEmpty
import assertk.assertions.isSuccess
import de.w3is.recipes.testUser
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

@MicronautTest
@Transactional
class InvitationServiceTest {

    @Inject
    private lateinit var invitationService: InvitationService

    @Inject
    private lateinit var userService: UserService

    @Test
    fun `when creating a invite then the creating user is recorded as creator`() {

        val invite = invitationService.createInvite(testUser)

        assertThat(invite.creator).isEqualTo(testUser.id)
        assertThat(invite.code).isNotEmpty()
    }

    @Test
    fun `when a user has created a invitation then she can create another one`() {

        assertThat {
            invitationService.createInvite(testUser)
            invitationService.createInvite(testUser)
        }.isSuccess()
    }

    @Test
    fun `when a person is invited than a user can be created`() {

        val userName = "newUser"

        val invite = invitationService.createInvite(testUser)

        invitationService.createUserByInvite(invite.code, userName, PlainPassword("password"))

        assertThat { userService.getUserFor(userName) }.isSuccess()
    }

    @Test
    fun `when a invite was used it can't be used again`() {

        val invite = invitationService.createInvite(testUser)

        invitationService.createUserByInvite(invite.code, "a", PlainPassword("password"))
        assertThat {
            invitationService.createUserByInvite(invite.code, "b", PlainPassword("password"))
        }.isFailure()
    }

    @Test
    fun `can't create user with non existing invite code`() {
        assertThat {
            invitationService.createUserByInvite("xyz", "a", PlainPassword("password"))
        }.isFailure()
    }
}