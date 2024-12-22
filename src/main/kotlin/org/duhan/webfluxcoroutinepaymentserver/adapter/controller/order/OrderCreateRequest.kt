package org.duhan.webfluxcoroutinepaymentserver.adapter.controller.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderCreateCommand
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderCreateCommand.ProductQuantityCommand

data class OrderCreateRequest(
    val userId: Long,
    val products: List<ProductQuantityRequest>,
) {
    fun toCommand(): OrderCreateCommand =
        OrderCreateCommand(
            userId = userId,
            products = products.map { ProductQuantityCommand(productId = it.productId, quantity = it.quantity) },
        )

    data class ProductQuantityRequest(
        val productId: Long,
        val quantity: Int,
    )
}
