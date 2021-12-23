package de.w3is.recipes.infra.views

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View

@Controller("/login")
@Secured(SecurityRule.IS_ANONYMOUS)
class LoginViewController {

    @Get
    @View("login")
    fun loginPage(): HttpResponse<Map<String, *>> = HttpResponse.ok(
        emptyMap<String, String>()
    )

    @Get("failed")
    @View("login")
    fun loginFailedPage(): HttpResponse<Map<String, *>> =
        HttpResponse.ok(mapOf("errors" to true))
}