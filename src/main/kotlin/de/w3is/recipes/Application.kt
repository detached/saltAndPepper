package de.w3is.recipes

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.info.License

@OpenAPIDefinition(
    info = Info(
        title = "Salt and Pepper",
        version = "1.0.0",
        description = "Salt and Pepper Rest API",
        license = License(
            name = "GPL-3.0",
            url = "https://www.gnu.org/licenses/gpl-3.0.en.html",
        ),
    ),
)
class Application {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            build()
                .args(*args)
                .packages("de.w3is.recipes")
                .start()
        }
    }
}
