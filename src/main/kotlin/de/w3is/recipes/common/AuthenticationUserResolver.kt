package de.w3is.recipes.common

import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.model.User
import de.w3is.recipes.users.model.UserId
import io.micronaut.security.authentication.Authentication

context(UserService)
fun Authentication.getUser(): User {
    return getUser(UserId(name))
}