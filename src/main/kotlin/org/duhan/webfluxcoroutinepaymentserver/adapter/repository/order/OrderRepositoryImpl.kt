package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderCrudRepository: OrderCrudRepository,
) : OrderRepository {
    override suspend fun save(order: Order): Order = orderCrudRepository.save(OrderEntity(order)).toModel()

    override suspend fun findByPgOrderId(pgOrderId: String): Order? = orderCrudRepository.findByPgOrderId(pgOrderId)?.toModel()
}
