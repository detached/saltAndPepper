package de.w3is.recipes.infra.views

import de.w3is.recipes.infra.views.model.Translations
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller("/login")
@Secured(SecurityRule.IS_ANONYMOUS)
class LoginViewController(private val translations: Translations) {

    @Get
    @View("login")
    fun loginPage(): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            mapOf(
                "translations" to translations
            )
        )

    @Get("failed")
    @View("login")
    fun loginFailedPage(): HttpResponse<Map<String, *>> =
        HttpResponse.ok(
            mapOf(
                "translations" to translations,
                "errors" to true
            )
        )
}