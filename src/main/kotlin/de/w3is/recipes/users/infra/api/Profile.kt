package de.w3is.recipes.users.infra.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Profile(
    val id: String,
    val name: String,
    val role: String,
    val isAllowedToInvite: Boolean,
)

@Serdeable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String,
)
