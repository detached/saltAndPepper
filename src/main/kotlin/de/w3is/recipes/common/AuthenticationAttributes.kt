package de.w3is.recipes.common

import de.w3is.recipes.users.model.User
import io.micronaut.security.authentication.Authentication

const val userAttribute = "user"

fun Authentication.getUser() = attributes[userAttribute] as User