package org.duhan.webfluxcoroutinepaymentserver.adapter.client

import org.duhan.webfluxcoroutinepaymentserver.adapter.client.TossPayConfirmRequest.Companion.of
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.TossPayApi
import org.duhan.webfluxcoroutinepaymentserver.core.order.service.OrderConfirmCommand
import org.springframework.stereotype.Component

@Component
class TossPayApiImpl : TossPayApi {
    override fun confirm(command: OrderConfirmCommand) {
        val request = of(command)
        println("request success")
    }
}
