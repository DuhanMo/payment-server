package org.duhan.webfluxcoroutinepaymentserver.common.filter

import io.micrometer.context.ContextRegistry
import org.duhan.webfluxcoroutinepaymentserver.common.extension.txid
import org.slf4j.MDC
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Hooks
import reactor.core.publisher.Mono
import reactor.util.context.Context
import java.util.UUID

const val KEY_TXID = "txid"

@Component
@Order(1)
class MdcFilter : WebFilter {
    init {
        propagateMdcThroughReactor()
    }

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        val uuid = exchange.request.headers["x-txid"]?.firstOrNull() ?: UUID.randomUUID().toString()
        MDC.put(KEY_TXID, uuid)
        exchange.request.txid = uuid
        return chain.filter(exchange)
            .contextWrite {
                Context.of(KEY_TXID, uuid)
            }.doFinally {
                MDC.remove(KEY_TXID)
                exchange.request.txid = null
            }
    }

    private fun propagateMdcThroughReactor() {
        Hooks.enableAutomaticContextPropagation()
        ContextRegistry.getInstance().registerThreadLocalAccessor(
            KEY_TXID,
            { MDC.get(KEY_TXID) },
            { value -> MDC.put(KEY_TXID, value) },
            { MDC.remove(KEY_TXID) },
        )
    }
}
