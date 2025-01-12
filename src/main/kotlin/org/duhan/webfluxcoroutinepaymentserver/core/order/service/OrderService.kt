package org.duhan.webfluxcoroutinepaymentserver.core.order.service

import mu.KotlinLogging
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.OrderProduct
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderProductRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.TossPayApi
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.getByPgOrderId
import org.duhan.webfluxcoroutinepaymentserver.core.product.port.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClientRequestException
import java.time.LocalDateTime.now
import java.util.UUID.randomUUID

private val logger = KotlinLogging.logger {}

@Service
class OrderService(
    private val tossPayApi: TossPayApi,
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val orderProductRepository: OrderProductRepository,
) {
    @Transactional
    suspend fun create(command: OrderCreateCommand): Order {
        val now = now()
        val productsById = productRepository.findAllById(command.products.map { it.productId }).associateBy { it.id }
        val notExistProductIds = command.products.map { it.productId } - productsById.keys
        require(notExistProductIds.isEmpty()) { "상품 식별자가 존재하지 않습니다. $notExistProductIds" }

        val amount = command.products.sumOf { productsById[it.productId]!!.price * it.quantity }
        val description =
            command.products.joinToString(", ") { "${productsById[it.productId]!!.name} x ${it.quantity}" }

        val newOrder =
            orderRepository.save(
                Order(
                    userId = command.userId,
                    amount = amount,
                    description = description,
                    pgOrderId = "${randomUUID()}".replace("-", ""),
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

    @Transactional
    suspend fun payAuth(command: OrderConfirmCommand): Order {
        val order = orderRepository.getByPgOrderId(command.pgOrderId)
        order.payAuth(command)
        orderRepository.save(order)
        return order
    }

    @Transactional
    suspend fun payConfirm(command: OrderConfirmCommand): Order {
        val order = orderRepository.getByPgOrderId(command.pgOrderId)
        order.captureRequest()
        try {
            tossPayApi.confirm(command)
            order.captureSuccess()
        } catch (e: Exception) {
            logger.error(e.message, e)
            when (e) {
                is WebClientRequestException -> order.captureRetry()
                else -> order.captureFail()
            }
        } finally {
            orderRepository.save(order)
        }
        return order
    }
}
