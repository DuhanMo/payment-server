package org.duhan.webfluxcoroutinepaymentserver.core.order.model

import java.time.LocalDateTime

data class OrderProduct(
    val id: Long? = null,
    val orderId: Long,
    val productId: Long,
    val price: Long,
    val quantity: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
