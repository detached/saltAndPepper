package de.w3is.recipes.application.users

import assertk.assertThat
import assertk.assertions.*
import de.w3is.recipes.testUser
import org.junit.jupiter.api.Test
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
}