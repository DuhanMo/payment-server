package org.duhan.webfluxcoroutinepaymentserver.domain.order.port

import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderConfirmCommand

interface TossPayApi {
    fun confirm(command: OrderConfirmCommand)
}
