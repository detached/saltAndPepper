package de.w3is.recipes.infra.views

import de.w3is.recipes.application.importer.ImportService
import de.w3is.recipes.infra.importer.GourmetRecipeSource
import de.w3is.recipes.infra.security.getUser
import de.w3is.recipes.infra.views.model.Menu
import de.w3is.recipes.infra.views.model.Site
import de.w3is.recipes.infra.views.model.Translations
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.micronaut.views.View
import reactor.core.publisher.Mono
import java.io.File

@Controller("/import")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ImportViewController(
    private val translations: Translations,
    private val importService: ImportService
) {

    @Get
    @View("import")
    fun getImportPage(): HttpResponse<Map<String, *>> = HttpResponse.ok(
        mapOf(
            "translations" to translations,
            "menu" to Menu(activeItem = Site.IMPORT),
            "error" to false,
        )
    )

    @Post(consumes = [MediaType.MULTIPART_FORM_DATA])
    @View("import")
    fun importFile(
        file: StreamingFileUpload,
        @QueryValue(value = "format", defaultValue = "gourmet") format: String,
        authentication: Authentication
    ): Mono<HttpResponse<*>> {

        val user = authentication.getUser()

        if (format != "gourmet") {
            TODO("Import other than gourmet format is not implemented")
        }

        val tempFile = File.createTempFile("upload", "temp")
        val upload = file.transferTo(tempFile)

        return Mono.from(upload).map { success ->
            if (success) {

                val gourmetFile = GourmetRecipeSource(tempFile.inputStream())
                importService.import(gourmetFile, user)

                HttpResponse.redirect<Unit>(searchUri)
            } else {
                HttpResponse.ok(
                    mapOf(
                        "translations" to translations,
                        "menu" to Menu(activeItem = Site.IMPORT),
                        "error" to true,
                    )
                )
            }
        }
    }
}