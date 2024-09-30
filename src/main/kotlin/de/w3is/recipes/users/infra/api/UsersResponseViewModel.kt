package de.w3is.recipes.users.infra.api

import de.w3is.recipes.common.api.PageViewModel
import de.w3is.recipes.users.model.Role
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UsersResponseViewModel(
    val users: List<UserViewModel>,
    val page: PageViewModel,
)

@Serdeable
data class UserViewModel(
    val id: String,
    val name: String,
    val role: Role,
)
