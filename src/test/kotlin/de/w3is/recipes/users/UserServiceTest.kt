package de.w3is.recipes.users

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import de.w3is.recipes.testUser
import de.w3is.recipes.users.infra.SimplePasswordValidator
import de.w3is.recipes.users.model.EncryptedPassword
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.Role
import de.w3is.recipes.users.model.User
import org.junit.jupiter.api.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserServiceTest {
    private val userRepository: UserRepository = mock()
    private val userService = UserService(userRepository, SimplePasswordValidator(5))

    @Test
    fun `a username can't be used again if it is already taken`() {
        val userName = "a"
        whenever(userRepository.findUser(userName)).thenReturn(testUser)
        assertFailure { userService.createNewUser(userName, PlainPassword("12345")) }
    }

    @Test
    fun `can't create user with empty username`() {
        assertFailure { userService.createNewUser(" ", PlainPassword("12345")) }
    }

    @Test
    fun `can't create user with empty password`() {
        assertFailure { userService.createNewUser("abc", PlainPassword(" ")) }
    }

    @Test
    fun `passwords have to be longer than limit`() {
        assertFailure { userService.createNewUser("abc", PlainPassword("1234")) }
        assertThat(userService.createNewUser("abc", PlainPassword("12345")))
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

        assertFailure {
            userService.changePassword(user, oldPassword, newPassword)
        }

        assertFailure {
            userService.changePassword(user, oldPassword, PlainPassword(""))
        }
    }

    @Test
    fun `when changing password and user provides wrong old password than throw error`() {
        val oldPassword = PlainPassword("12345")
        val newPassword = PlainPassword("67890")
        val user = givenUser("abc", oldPassword)

        assertFailure {
            userService.changePassword(user, PlainPassword("aWrongOne"), newPassword)
        }
    }

    private fun givenUser(
        username: String,
        password: PlainPassword,
    ): User =
        User.createNew(username, password, role = Role.USER).also {
            whenever(userRepository.findUser(it.name)).thenReturn(it)
        }
}
