package de.w3is.recipes.users.infra.api

data class Profile(
    val id: String,
    val name: String,
    val role: String,
    val isAllowedToInvite: Boolean,
)

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
)
