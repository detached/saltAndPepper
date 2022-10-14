package de.w3is.recipes.common

import io.micronaut.http.HttpRequest
import io.micronaut.http.MutableHttpResponse
import io.micronaut.http.annotation.Filter
import io.micronaut.http.filter.HttpServerFilter
import io.micronaut.http.filter.ServerFilterChain
import io.micronaut.http.filter.ServerFilterPhase
import org.apache.velocity.tools.generic.EscapeTool
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

/**
 * Escape every string in the model
 */
@Filter(Filter.MATCH_ALL_PATTERN)
class AntiXSSFilter : HttpServerFilter {

    private val escapeTool = EscapeTool()

    override fun getOrder(): Int = ServerFilterPhase.LAST.order()

    override fun doFilter(request: HttpRequest<*>, chain: ServerFilterChain): Publisher<MutableHttpResponse<*>> =
        Flux.from(chain.proceed(request)).switchMap { response ->
            val body = response.body()
            if (body != null && body is Map<*, *>) {

                val escapedModel = body.mapValues { (_, value) ->
                    if (value is String) {
                        escapeTool.html(value)
                    } else {
                        value
                    }
                }.toMap()

                response.body(escapedModel)
            }
            Flux.just(response)
        }
}