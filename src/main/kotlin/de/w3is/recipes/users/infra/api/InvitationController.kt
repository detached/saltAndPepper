package de.w3is.recipes.users.infra.api

import de.w3is.recipes.common.getUser
import de.w3is.recipes.users.InvitationService
import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Singleton

@Singleton
@Controller("/api/invitation")
class InvitationController(
    private val userService: UserService,
    private val invitationService: InvitationService,
) {

    @Get
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Get own invite code if any", operationId = "getExistingInviteCode")
    @Tag(name = "invitations")
    fun getExistingInviteCode(
        authentication: Authentication,
    ): HttpResponse<InvitationCodeResponse> {
        val user = with(userService) { authentication.getUser() }
        val invite = invitationService.findExistingInvite(user)

        return if (invite == null) {
            HttpResponse.noContent()
        } else {
            HttpResponse.ok(InvitationCodeResponse(invite.code))
        }
    }

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Create new invite link", operationId = "createInviteLink")
    @Tag(name = "invitations")
    fun createInviteLink(
        authentication: Authentication,
    ): InvitationCodeResponse {
        val user = with(userService) { authentication.getUser() }
        val invite = invitationService.createInvite(user)

        return InvitationCodeResponse(invite.code)
    }

    @Get("/{code}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Get invite information by code", operationId = "getInvitationInfo")
    @Tag(name = "invitations")
    fun getInvitationInfo(@PathVariable("code") inviteCode: String): InvitationInfoResponse {
        val invite = invitationService.getInviteByCode(inviteCode)
        val invitingUser = userService.getUser(invite.creator)
        return InvitationInfoResponse(
            invitingUser = invitingUser.name,
        )
    }

    @Put("/{code}")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Create new user by invitation code", operationId = "useInvitation")
    @Tag(name = "invitations")
    fun useInvitation(
        @PathVariable("code") inviteCode: String,
        @Body invitationRequest: InvitationRequest,
    ) {
        invitationService.createUserByInvite(
            inviteCode,
            invitationRequest.username,
            PlainPassword(invitationRequest.password),
        )
    }
}
