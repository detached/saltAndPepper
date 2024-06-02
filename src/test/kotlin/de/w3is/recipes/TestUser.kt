package de.w3is.recipes

import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.Role
import de.w3is.recipes.users.model.User

val testUser = User.createNew("testUser", PlainPassword("testPassword"), role = Role.USER)
