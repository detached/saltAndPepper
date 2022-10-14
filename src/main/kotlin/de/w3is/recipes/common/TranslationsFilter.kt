package de.w3is.recipes.common

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.filter.ServerFilterPhase
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

/**
 * Adds the translations instance to the model if a view can be resolved for the response
 */
@Filter(Filter.MATCH_ALL_PATTERN)
class TranslationsFilter(
    private val translations: Translations
) : HttpServerFilter {

    override fun getOrder(): Int = ServerFilterPhase.LAST.order()

    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> =
        Flux.from(chain.proceed(request)).switchMap { response ->

            val originalBody = response.body()

            if (originalBody != null && originalBody is Map<*, *>) {
                response.body(originalBody + ("translations" to translations))
            }

            Flux.just(response)
        }
}