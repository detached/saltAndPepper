package de.w3is.recipes

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.uri.UriBuilder
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.security.Principal

@Controller("/")
@Secured(SecurityRule.IS_ANONYMOUS)
class IndexViewController {

    @Get
    fun getIndex(principal: Principal?): HttpResponse<Map<String, *>> =
        if (principal == null) {
            HttpResponse.redirect(UriBuilder.of("/login").build())
        } else {
            HttpResponse.redirect(UriBuilder.of("/search").build())
        }
}