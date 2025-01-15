package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.OrderProduct
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("order_product")
data class OrderProductEntity(
    @Id
    val id: Long?,
    val orderId: Long,
    val productId: Long,
    val price: Long,
    val quantity: Int,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(orderProduct: OrderProduct) : this(
        id = orderProduct.id,
        orderId = orderProduct.orderId,
        productId = orderProduct.productId,
        price = orderProduct.price,
        quantity = orderProduct.quantity,
        createdAt = orderProduct.createdAt,
        updatedAt = orderProduct.updatedAt,
    )

    fun toModel(): OrderProduct =
        OrderProduct(
            id = id,
            orderId = orderId,
            productId = productId,
            price = price,
            quantity = quantity,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
