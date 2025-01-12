package org.duhan.webfluxcoroutinepaymentserver.core.order.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.coEvery
import io.mockk.mockk
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderProductRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.OrderRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.port.TossPayApi
import org.duhan.webfluxcoroutinepaymentserver.core.order.service.OrderCreateCommand.ProductQuantityCommand
import org.duhan.webfluxcoroutinepaymentserver.core.product.port.ProductRepository

class OrderServiceTest : BehaviorSpec({
    val tossPayApi = mockk<TossPayApi>()
    val orderRepository = mockk<OrderRepository>()
    val productRepository = mockk<ProductRepository>()
    val orderProductRepository = mockk<OrderProductRepository>()

    val sut = OrderService(tossPayApi, orderRepository, productRepository, orderProductRepository)

    Given("존재하지 않는 식별자인 경우") {
        coEvery { productRepository.findAllById(any()) } returns emptyList()

        val command = OrderCreateCommand(100L, listOf(ProductQuantityCommand(1L, 2), ProductQuantityCommand(2L, 2)))

        When("주문을 생성하면") {
            Then("예외 발생한다") {
                shouldThrow<IllegalArgumentException> { sut.create(command) }
            }
        }
    }
})
