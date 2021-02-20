package de.w3is.recipes

import de.w3is.recipes.application.users.Role
import de.w3is.recipes.application.users.User

val testUser = User.createNew("testUser", "testPassword", role = Role.USER)
val testAdmin = User.createNew("testAdmin", "testAdmin", role = Role.ADMIN)