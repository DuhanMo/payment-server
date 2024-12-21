package org.duhan.webfluxcoroutinepaymentserver.domain.order.port

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order

interface OrderRepository {
    suspend fun save(order: Order): Order
}
