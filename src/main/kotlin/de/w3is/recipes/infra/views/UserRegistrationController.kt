package de.w3is.recipes.infra.views

import de.w3is.recipes.application.users.*
import de.w3is.recipes.infra.views.model.Translations
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import java.net.URI

@Controller("/registration")
@Secured(SecurityRule.IS_ANONYMOUS)
class UserRegistrationController(
    private val translations: Translations,
    private val invitationService: InvitationService,
    private val userService: UserService,
) {

    @Get("/invite/{code}")
    @View("inviteRegistration")
    fun showInviteRegistration(
        @PathVariable("code") code: String,
        authentication: Authentication?
    ): HttpResponse<Map<String, *>> {

        if (authentication != null) {
            // Already registered users are redirected to /
            return HttpResponse.temporaryRedirect(URI("/"))
        }

        return HttpResponse.ok(buildModelForInvite(code))
    }

    @Post("/invite/{code}", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    @View("userRegistered")
    fun registerUser(
        @PathVariable("code") code: String,
        @RequestAttribute("name") name: String,
        @RequestAttribute("password") plainPassword: String,
        authentication: Authentication?
    ): HttpResponse<Map<String, *>> {

        if (authentication != null) {
            // Already registered users are redirected to /
            return HttpResponse.temporaryRedirect(URI("/"))
        }

        val user = invitationService.createUserByInvite(code, name, PlainPassword(plainPassword))

        return HttpResponse.ok(
            mapOf("name" to user.name)
        )
    }

    @Error(exception = UserAlreadyExistsException::class)
    @View("inviteRegistration")
    fun handleUserAlreadyExistsException(@PathVariable("code") code: String): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            buildModelForInvite(code)
                    + ("errorText" to translations.get("inviteRegistration.error.userAlreadyExists"))
        )

    @Error(exception = PasswordTooWeekException::class)
    @View("inviteRegistration")
    fun handlePasswordToWeekException(@PathVariable("code") code: String): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            buildModelForInvite(code)
                    + ("errorText" to translations.get("inviteRegistration.error.passwordTooWeek"))
        )

    @Error(exception = UsernameInvalidException::class)
    @View("inviteRegistration")
    fun handleUsernameInvalidException(@PathVariable("code") code: String): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            buildModelForInvite(code)
                    + ("errorText" to translations.get("inviteRegistration.error.usernameInvalid"))
        )

    @Error(exception = InvitationNotFoundException::class)
    fun handleInvitationNotFoundException(): HttpResponse<Map<String, *>> = HttpResponse.notFound()

    private fun buildModelForInvite(code: String): Map<String, *> {
        val invite = invitationService.getInviteByCode(code)
        val invitingUser = userService.getUser(invite.creator)

        return mapOf("invitingUser" to invitingUser.name)
    }
}

fun buildInviteLink(host: String, code: String): String =
    UriBuilder.of(host).path("/registration/invite").path(code).build().toString()