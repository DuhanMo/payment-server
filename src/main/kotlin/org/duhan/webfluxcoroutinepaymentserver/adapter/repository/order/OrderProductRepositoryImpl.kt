package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.OrderProduct
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderProductRepository
import org.springframework.stereotype.Repository

@Repository
class OrderProductRepositoryImpl(
    private val orderProductCrudRepository: OrderProductCrudRepository,
) : OrderProductRepository {
    override suspend fun save(orderProduct: OrderProduct): OrderProduct =
        orderProductCrudRepository.save(OrderProductEntity(orderProduct)).toModel()

    override suspend fun findAll(): List<OrderProduct> = orderProductCrudRepository.findAll().map { it.toModel() }.toList()
}
