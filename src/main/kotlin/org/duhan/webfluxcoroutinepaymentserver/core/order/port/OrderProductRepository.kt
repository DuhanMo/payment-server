package org.duhan.webfluxcoroutinepaymentserver.core.order.port

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.OrderProduct

interface OrderProductRepository {
    suspend fun save(orderProduct: OrderProduct): OrderProduct

    suspend fun findAll(): List<OrderProduct>
}
