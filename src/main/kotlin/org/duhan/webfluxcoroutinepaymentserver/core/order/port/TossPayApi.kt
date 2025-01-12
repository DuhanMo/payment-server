package org.duhan.webfluxcoroutinepaymentserver.core.order.port

import org.duhan.webfluxcoroutinepaymentserver.core.order.service.OrderConfirmCommand

interface TossPayApi {
    fun confirm(command: OrderConfirmCommand)
}
