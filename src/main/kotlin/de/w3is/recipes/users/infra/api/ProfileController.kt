package de.w3is.recipes.users.infra.api

import de.w3is.recipes.common.getUser
import de.w3is.recipes.users.InvitationService
import de.w3is.recipes.users.PasswordTooWeekException
import de.w3is.recipes.users.UserService
import de.w3is.recipes.users.WrongPasswordException
import de.w3is.recipes.users.model.PlainPassword
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Patch
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Singleton

@Singleton
@Controller("/api/profile")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ProfileController(
    private val invitationService: InvitationService,
    private val userService: UserService,
) {

    @Get
    fun getProfile(authentication: Authentication): Profile {
        val user = with(userService) { authentication.getUser() }

        val allowedToInvite = invitationService.isAllowedToInvite(user)

        return Profile(
            id = user.id.value,
            name = user.name,
            role = user.role.name,
            isAllowedToInvite = allowedToInvite,
        )
    }

    @Patch("/password")
    fun changePassword(@Body request: ChangePasswordRequest, authentication: Authentication): HttpResponse<Unit> {
        val user = with(userService) { authentication.getUser() }

        return try {
            userService.changePassword(user, PlainPassword(request.oldPassword), PlainPassword(request.newPassword))
            HttpResponse.status(HttpStatus.ACCEPTED)
        } catch (e: PasswordTooWeekException) {
            HttpResponse.status(HttpStatus.BAD_REQUEST, "Password to week")
        } catch (e: WrongPasswordException) {
            HttpResponse.status(HttpStatus.BAD_REQUEST, "Wrong password")
        }
    }
}
