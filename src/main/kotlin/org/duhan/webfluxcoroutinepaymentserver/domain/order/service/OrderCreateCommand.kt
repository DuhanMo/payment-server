package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

data class OrderCreateCommand(
    val userId: Long,
    val products: List<ProductQuantityCommand>,
) {
    data class ProductQuantityCommand(
        val productId: Long,
        val quantity: Int,
    )
}
