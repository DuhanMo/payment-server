package org.duhan.webfluxcoroutinepaymentserver.core.order.model

import java.time.Instant

data class OrderProduct(
    val id: Long? = null,
    val orderId: Long,
    val productId: Long,
    val price: Long,
    val quantity: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
)
