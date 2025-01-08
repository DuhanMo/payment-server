package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface OrderCrudRepository : CoroutineCrudRepository<OrderEntity, Long> {
    suspend fun findByPgOrderId(pgOrderId: String): OrderEntity?
}
