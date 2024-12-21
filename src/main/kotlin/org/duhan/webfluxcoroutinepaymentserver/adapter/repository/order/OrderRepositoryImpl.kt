package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.port.OrderRepository
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderCrudRepository: OrderCrudRepository,
) : OrderRepository {
    override suspend fun save(order: Order): Order = orderCrudRepository.save(OrderEntity(order)).toModel()
}
