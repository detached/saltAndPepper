package de.w3is.recipes.utils

import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.User
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken

interface TestUserProvider {

    val userService: UserService
    val authenticationClient: AuthenticationClient

    fun setupUser(userName: String, password: String): Pair<User, BearerAccessRefreshToken> {
        val user = userService.createNewUser(userName, PlainPassword(password))
        val token = authenticationClient.login(UsernamePasswordCredentials(userName, password))
        return user to token
    }
}

@Client("/api")
interface AuthenticationClient {

    @Post("/login")
    fun login(@Body credentials: UsernamePasswordCredentials): BearerAccessRefreshToken
}
