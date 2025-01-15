package org.duhan.webfluxcoroutinepaymentserver.fixture

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.OrderProduct
import java.time.Instant.now

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
        createdAt = now(),
        updatedAt = now(),
    )
}

fun createOrderProduct(
    id: Long? = null,
    orderId: Long,
    productId: Long,
    price: Long = 500L,
    quantity: Int = 1,
): OrderProduct {
    return OrderProduct(
        id = id,
        orderId = orderId,
        productId = productId,
        price = price,
        quantity = quantity,
        createdAt = now(),
        updatedAt = now(),
    )
}
