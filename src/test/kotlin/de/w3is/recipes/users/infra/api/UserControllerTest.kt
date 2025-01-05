package de.w3is.recipes.users.infra.api

import assertk.all
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import assertk.assertions.prop
import de.w3is.recipes.common.api.PageViewModel
import de.w3is.recipes.users.UserService
import de.w3is.recipes.utils.AuthenticationClient
import de.w3is.recipes.utils.DBCleaner
import de.w3is.recipes.utils.TestUserProvider
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.kotlin.http.retrieveObject
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.jooq.DSLContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@MicronautTest
class UserControllerTest(
    @Client("/api") val client: HttpClient,
    @Inject override val userService: UserService,
    @Inject override val authenticationClient: AuthenticationClient,
    @Inject override val dslContext: DSLContext,
) : TestUserProvider, DBCleaner {
    private val userName = "testUser"
    private val password = "password"

    @BeforeEach
    fun setUp() {
        cleanDb()
    }

    @Test
    fun `test only logged-in user is returned when no users are added`() {
        // Arrange
        val (user, token) = givenAuthenticatedUser(userName, password)

        // Act
        val response =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        // Assert
        assertThat(response.users).hasSize(1)
        assertThat(response.users.first()).all {
            prop(UserViewModel::id).isEqualTo(user.id.value)
            prop(UserViewModel::name).isEqualTo(user.name)
            prop(UserViewModel::role).isEqualTo(user.role)
        }
        assertThat(response.page).all {
            prop(PageViewModel::size).isEqualTo(1)
            prop(PageViewModel::number).isEqualTo(0)
            prop(PageViewModel::maxNumber).isEqualTo(0)
        }
    }

    @Test
    fun `test multiple users are fetched correctly by pagination parameters`() {
        // Arrange
        val (user1, token) = givenAuthenticatedUser(userName, password)
        val user2 = givenUser("user2")
        val user3 = givenUser("user3")
        val user4 = givenUser("user4")

        // Act
        val firstPage =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users?page=0&limit=2")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )
        val secondPage =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users?page=1&limit=2")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        // Assert
        assertThat(firstPage.users).hasSize(2)
        assertThat(firstPage.users[0].id).isEqualTo(user1.id.value)
        assertThat(firstPage.users[1].id).isEqualTo(user2.id.value)
        assertThat(firstPage.page).all {
            prop(PageViewModel::size).isEqualTo(2)
            prop(PageViewModel::number).isEqualTo(0)
            prop(PageViewModel::maxNumber).isEqualTo(1)
        }

        assertThat(secondPage.users).hasSize(2)
        assertThat(secondPage.users[0].id).isEqualTo(user3.id.value)
        assertThat(secondPage.users[1].id).isEqualTo(user4.id.value)
        assertThat(secondPage.page).all {
            prop(PageViewModel::size).isEqualTo(2)
            prop(PageViewModel::number).isEqualTo(1)
            prop(PageViewModel::maxNumber).isEqualTo(1)
        }
    }

    @Test
    fun `test huge page limit is sanitized into a smaller page size`() {
        // Arrange
        val (_, token) = givenAuthenticatedUser(userName, password)
        (0..50).forEach { givenUser("user$it") }

        // Act
        val response =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users?page=0&limit=1000")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        // Assert
        assertThat(response.users).hasSize(50)
        assertThat(response.page).all {
            prop(PageViewModel::size).isEqualTo(50)
            prop(PageViewModel::number).isEqualTo(0)
            prop(PageViewModel::maxNumber).isEqualTo(1)
        }
    }

    @Test
    fun `test negative page value is set to 0`() {
        // Arrange
        val (_, token) = givenAuthenticatedUser(userName, password)

        // Act
        val response =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users?page=-1&limit=10")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        // Assert
        assertThat(response.users).hasSize(1)
        assertThat(response.page).all {
            prop(PageViewModel::size).isEqualTo(1)
            prop(PageViewModel::number).isEqualTo(0)
            prop(PageViewModel::maxNumber).isEqualTo(0)
        }
    }

    @Test
    fun `test negative limit value is set to a positive value`() {
        // Arrange
        val (_, token) = givenAuthenticatedUser(userName, password)

        // Act
        val response =
            client.toBlocking().retrieveObject<UsersResponseViewModel>(
                HttpRequest.GET<Any>("/users?page=0&limit=-1")
                    .bearerAuth(token.accessToken)
                    .accept("application/json"),
            )

        // Assert
        assertThat(response.users).hasSize(1)
        assertThat(response.page).all {
            prop(PageViewModel::size).isEqualTo(1)
            prop(PageViewModel::number).isEqualTo(0)
            prop(PageViewModel::maxNumber).isEqualTo(0)
        }
    }
}
