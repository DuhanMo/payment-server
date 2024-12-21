package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

data class OrderCreateCommand(
    val userId: Long,
    val products: List<ProductQuantity>,
)

data class ProductQuantity(
    val productId: Long,
    val quantity: Int,
)
