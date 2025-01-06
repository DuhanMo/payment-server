package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.OrderProduct
import org.duhan.webfluxcoroutinepaymentserver.domain.order.port.OrderProductRepository
import org.duhan.webfluxcoroutinepaymentserver.domain.order.port.OrderRepository
import org.duhan.webfluxcoroutinepaymentserver.domain.product.port.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime.now
import java.util.UUID.randomUUID

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val orderProductRepository: OrderProductRepository,
) {
    @Transactional
    suspend fun create(command: OrderCreateCommand): Order {
        val now = now()
        val productsById = productRepository.findAllById(command.products.map { it.productId }).associateBy { it.id }
        val notExistProductIds = command.products.map { it.productId } - productsById.keys
        if (notExistProductIds.isNotEmpty()) {
            throw IllegalArgumentException("상품 식별자가 존재하지 않습니다. $notExistProductIds")
        }
        command.products.map { it.productId !in productsById.keys }

        val amount = command.products.sumOf { productsById[it.productId]!!.price * it.quantity }
        val description =
            command.products.joinToString(", ") { "${productsById[it.productId]!!.name} x ${it.quantity}" }

        val newOrder =
            orderRepository.save(
                Order(
                    userId = command.userId,
                    amount = amount,
                    description = description,
                    pgOrderId = "${randomUUID()}".replace("-",""),
                    createdAt = now,
                    updatedAt = now,
                ),
            )

        command.products.forEach {
            orderProductRepository.save(
                OrderProduct(
                    orderId = newOrder.id!!,
                    productId = it.productId,
                    price = productsById[it.productId]!!.price,
                    quantity = it.quantity,
                    createdAt = now,
                    updatedAt = now,
                ),
            )
        }
        return newOrder
    }
}
