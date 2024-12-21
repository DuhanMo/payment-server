package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.port.OrderRepository
import org.duhan.webfluxcoroutinepaymentserver.domain.product.port.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime.now

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
) {
    @Transactional
    suspend fun create(command: OrderCreateCommand): Order {
        val now = now()
        val productsById = productRepository.findAllById(command.products.map { it.productId }).associateBy { it.id }
        val amount = command.products.sumOf { productsById[it.productId]!!.price * it.quantity }
        val description =
            command.products.joinToString(", ") { "${productsById[it.productId]!!.name} x ${it.quantity}" }

        val newOrder =
            orderRepository.save(
                Order(
                    userId = command.userId,
                    amount = amount,
                    description = description,
                    createdAt = now,
                    updatedAt = now,
                ),
            )

        // TODO: 주문-상품 저장
        return newOrder
    }
}
