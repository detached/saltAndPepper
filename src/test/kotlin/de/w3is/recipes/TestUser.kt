package de.w3is.recipes

import de.w3is.recipes.application.users.PlainPassword
import de.w3is.recipes.application.users.Role
import de.w3is.recipes.application.users.User

val testUser = User.createNew("testUser", PlainPassword("testPassword"), role = Role.USER)
val testAdmin = User.createNew("testAdmin", PlainPassword("testAdmin"), role = Role.ADMIN)