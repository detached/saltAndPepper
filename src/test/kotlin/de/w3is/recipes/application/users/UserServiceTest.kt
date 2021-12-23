package de.w3is.recipes.application.users

import assertk.all
import assertk.assertThat
import assertk.assertions.*
import de.w3is.recipes.testUser
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*

class UserServiceTest {

    private val userRepository: UserRepository = mock()
    private val userService = UserService(userRepository, SimplePasswordValidator(5))

    @Test
    fun `a username can't be used again if it is already taken`() {

        val userName = "a"
        whenever(userRepository.findUser(userName)).thenReturn(testUser)
        assertThat { userService.createNewUser(userName, PlainPassword("12345")) }.isFailure()
    }

    @Test
    fun `can't create user with empty username`() {
        assertThat { userService.createNewUser(" ", PlainPassword("12345")) }.isFailure()
    }

    @Test
    fun `can't create user with empty password`() {
        assertThat { userService.createNewUser("abc", PlainPassword(" ")) }.isFailure()
    }

    @Test
    fun `passwords have to be longer than limit`() {
        assertThat { userService.createNewUser("abc", PlainPassword("1234")) }.isFailure()
        assertThat { userService.createNewUser("abc", PlainPassword("12345")) }.isSuccess()
    }

    @Test
    fun `test create user`() {

        val user = userService.createNewUser("abc", PlainPassword("12345"))

        verify(userRepository).store(user)

        assertThat(user.name).isEqualTo("abc")
        assertThat(user.password)
            .isInstanceOf(EncryptedPassword::class)
            .transform { it.value }.isNotEqualTo("12345")
    }

    @Test
    fun `test change password`() {

        val oldPassword = PlainPassword("12345")
        val newPassword = PlainPassword("67890")
        val user = givenUser("abc", oldPassword)

        userService.changePassword(user, oldPassword, newPassword)

        val argumentCaptor = argumentCaptor<User>()

        verify(userRepository).update(argumentCaptor.capture())

        assertThat(argumentCaptor.firstValue.id).isEqualTo(user.id)
        assertThat(argumentCaptor.firstValue.authenticate(oldPassword)).isFalse()
        assertThat(argumentCaptor.firstValue.authenticate(newPassword)).isTrue()
    }

    @Test
    fun `when changing password then enforce password rules`() {

        val oldPassword = PlainPassword("12345")
        val newPassword = PlainPassword("67")
        val user = givenUser("abc", oldPassword)

        assertThat {
            userService.changePassword(user, oldPassword, newPassword)
        }.isFailure()

        assertThat {
            userService.changePassword(user, oldPassword, PlainPassword(""))
        }.isFailure()
    }

    @Test
    fun `when changing password and user provides wrong old password than throw error`() {

        val oldPassword = PlainPassword("12345")
        val newPassword = PlainPassword("67890")
        val user = givenUser("abc", oldPassword)

        assertThat {
            userService.changePassword(user, PlainPassword("aWrongOne"), newPassword)
        }.isFailure()
    }

    private fun givenUser(username: String, password: PlainPassword): User =
        User.createNew(username, password, role = Role.USER).also {
            whenever(userRepository.findUser(it.name)).thenReturn(it)
        }
}