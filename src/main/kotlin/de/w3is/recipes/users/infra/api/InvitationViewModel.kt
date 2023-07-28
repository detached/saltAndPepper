package de.w3is.recipes.users.infra.api

data class InvitationCodeResponse(
    val code: String,
)

data class InvitationInfoResponse(
    val invitingUser: String,
)

data class InvitationRequest(
    val username: String,
    val password: String,
)
