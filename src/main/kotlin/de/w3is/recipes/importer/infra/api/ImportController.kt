package de.w3is.recipes.importer.infra.api

import de.w3is.recipes.common.getUser
import de.w3is.recipes.importer.ImportService
import de.w3is.recipes.importer.GourmetRecipeSource
import de.w3is.recipes.users.UserService
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.multipart.StreamingFileUpload
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.io.File

@Controller("/api/import")
@Secured(SecurityRule.IS_AUTHENTICATED)
class ImportController(
    private val userService: UserService,
    private val importService: ImportService
) {
    @Post("/gourmet", consumes = [MediaType.MULTIPART_FORM_DATA])
    fun importGourmetXml(
        file: StreamingFileUpload,
        authentication: Authentication
    ): Mono<HttpResponse<*>> {

        val user = with(userService) { authentication.getUser() }

        return Mono.fromCallable { File.createTempFile("upload", "temp") }
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap { tempFile ->
                Mono.from(file.transferTo(tempFile)).map { success ->
                    tempFile to success
                }
            }.map { (tempFile, success) ->
                if (success) {
                    val gourmetFile = GourmetRecipeSource(tempFile.inputStream())
                    importService.import(gourmetFile, user)
                }
                HttpResponse.noContent<Unit>()
            }
    }
}