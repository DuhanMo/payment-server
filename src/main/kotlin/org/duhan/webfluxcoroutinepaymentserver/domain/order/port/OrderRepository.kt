package org.duhan.webfluxcoroutinepaymentserver.domain.order.port

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order

suspend fun OrderRepository.getByPgOrderId(pgOrderId: String): Order =
    findByPgOrderId(pgOrderId)
        ?: throw IllegalArgumentException("Order with id $pgOrderId not found")

interface OrderRepository {
    suspend fun save(order: Order): Order

    suspend fun findByPgOrderId(pgOrderId: String): Order?
}
