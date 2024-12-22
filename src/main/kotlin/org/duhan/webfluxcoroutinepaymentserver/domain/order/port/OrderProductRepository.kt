package org.duhan.webfluxcoroutinepaymentserver.domain.order.port

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.OrderProduct

interface OrderProductRepository {
    suspend fun save(orderProduct: OrderProduct): OrderProduct

    suspend fun findAll(): List<OrderProduct>
}
