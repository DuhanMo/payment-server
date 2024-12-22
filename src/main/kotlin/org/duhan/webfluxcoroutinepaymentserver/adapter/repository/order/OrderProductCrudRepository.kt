package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OrderProductCrudRepository : CoroutineCrudRepository<OrderProductEntity, Long> {
    suspend fun save(orderProductEntity: OrderProductEntity): OrderProductEntity
}
