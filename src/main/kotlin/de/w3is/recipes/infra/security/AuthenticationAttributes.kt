package de.w3is.recipes.infra.security

import de.w3is.recipes.application.users.User
import io.micronaut.security.authentication.Authentication

const val userAttribute = "user"

fun Authentication.getUser() = attributes[userAttribute] as User