package de.w3is.recipes.users.infra.view

import de.w3is.recipes.common.Menu
import de.w3is.recipes.common.Site
import de.w3is.recipes.users.infra.view.model.ProfileViewModel
import de.w3is.recipes.common.Translations
import de.w3is.recipes.common.getUser
import de.w3is.recipes.users.*
import de.w3is.recipes.users.model.Invite
import de.w3is.recipes.users.model.PlainPassword
import de.w3is.recipes.users.model.User
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller("/profile")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ProfileViewController(
    private val invitationService: InvitationService,
    private val userService: UserService,
    private val httpHostResolver: HttpHostResolver,
    private val translations: Translations,
) {

    @Get
    @View("profile")
    fun showProfile(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> =
        HttpResponse.ok(buildProfileModel(request, authentication.getUser()))

    @Post("/invitation", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    @View("profile")
    fun createInvitationLink(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> {

        val user = authentication.getUser()
        val invite = invitationService.createInvite(user)

        return HttpResponse.ok(buildProfileModel(request, user, invite))
    }

    @Post("/changePassword", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    @View("profile")
    fun changePassword(
        request: HttpRequest<*>,
        authentication: Authentication,
        @RequestAttribute("oldPassword") oldPassword: String,
        @RequestAttribute("newPassword") newPassword: String
    ): HttpResponse<Map<String, *>> {

        val user = authentication.getUser()
        userService.changePassword(user, PlainPassword(oldPassword), PlainPassword(newPassword))

        return HttpResponse.ok(buildProfileModel(request, user) +
                ("passwordChangeSuccess" to true))
    }

    @Error(exception = PasswordTooWeekException::class)
    @View("profile")
    fun handlePasswordToWeekException(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            buildProfileModel(request, authentication.getUser()) +
                    ("passwordChangeError" to translations.get("password.error.passwordTooWeek"))
        )

    @Error(exception = WrongPasswordException::class)
    @View("profile")
    fun handleWrongPasswordException(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            buildProfileModel(request, authentication.getUser()) +
                    ("passwordChangeError" to translations.get("password.error.wrongPassword"))
        )

    private fun buildProfileModel(
        request: HttpRequest<*>,
        user: User,
        invite: Invite? = null
    ): Map<String, *> {

        val model = mutableMapOf(
            "profile" to ProfileViewModel(
                name = user.name,
                role = user.role.name,
            ),
            "allowedToInvite" to invitationService.isAllowedToInvite(user),
            "menu" to Menu(activeItem = Site.PROFILE)
        )

        if (invite != null) {
            model["invitationLink"] = buildInviteLink(host = httpHostResolver.resolve(request), code = invite.code)
            model["invite"] = invite
        }

        return model
    }
}