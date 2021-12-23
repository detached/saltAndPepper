package de.w3is.recipes.infra.views

import de.w3is.recipes.application.users.InvitationService
import de.w3is.recipes.application.users.Invite
import de.w3is.recipes.application.users.User
import de.w3is.recipes.infra.security.getUser
import de.w3is.recipes.infra.views.model.Menu
import de.w3is.recipes.infra.views.model.ProfileViewModel
import de.w3is.recipes.infra.views.model.Site
import de.w3is.recipes.infra.views.model.Translations
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.server.util.HttpHostResolver
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller("/profile")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ProfileViewController(
    private val translations: Translations,
    private val invitationService: InvitationService,
    private val httpHostResolver: HttpHostResolver
) {

    @Get
    @View("profile")
    fun showProfile(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> =
        buildProfileModel(request, authentication.getUser())

    @Post("/invitation", consumes = [MediaType.APPLICATION_FORM_URLENCODED])
    @View("profile")
    fun createInvitationLink(request: HttpRequest<*>, authentication: Authentication): HttpResponse<Map<String, *>> {

        val user = authentication.getUser()
        val invite = invitationService.createInvite(user)

        return buildProfileModel(request, user, invite)
    }

    private fun buildProfileModel(
        request: HttpRequest<*>,
        user: User,
        invite: Invite? = null
    ): MutableHttpResponse<Map<String, *>> {

        val model = mutableMapOf(
            "profile" to ProfileViewModel(
                name = user.name,
                role = user.role.name,
            ),
            "allowedToInvite" to invitationService.isAllowedToInvite(user),
            "translations" to translations,
            "menu" to Menu(activeItem = Site.PROFILE)
        )

        if (invite != null) {
            model["invitationLink"] = buildInviteLink(host = httpHostResolver.resolve(request), code = invite.code)
            model["invite"] = invite
        }

        return HttpResponse.ok(model)
    }
}