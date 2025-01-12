package org.duhan.webfluxcoroutinepaymentserver.fixture

import java.time.LocalDateTime
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.Order

fun createOrder(
    userId: Long = 1L,
    amount: Long = 1000L,
    description: String = "description",
    pgOrderId: String = "pgOrderId",
): Order {
    return Order(
        userId = userId,
        amount = amount,
        description = description,
        pgOrderId = pgOrderId,
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now(),
    )
}