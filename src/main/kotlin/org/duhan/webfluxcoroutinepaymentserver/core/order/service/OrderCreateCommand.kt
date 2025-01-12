package org.duhan.webfluxcoroutinepaymentserver.core.order.service

data class OrderCreateCommand(
    val userId: Long,
    val products: List<ProductQuantityCommand>,
) {
    data class ProductQuantityCommand(
        val productId: Long,
        val quantity: Int,
    )
}
