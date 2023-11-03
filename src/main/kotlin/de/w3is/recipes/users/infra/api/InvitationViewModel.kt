package de.w3is.recipes.users.infra.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class InvitationCodeResponse(
    val code: String,
)

@Serdeable
data class InvitationInfoResponse(
    val invitingUser: String,
)

@Serdeable
data class InvitationRequest(
    val username: String,
    val password: String,
)
