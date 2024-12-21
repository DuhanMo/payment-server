package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import org.duhan.webfluxcoroutinepaymentserver.domain.order.mock.FakeOrderRepository
import org.duhan.webfluxcoroutinepaymentserver.domain.product.FakeProductRepository
import org.duhan.webfluxcoroutinepaymentserver.fixture.createProduct

class OrderServiceTest : BehaviorSpec({
    lateinit var sut: OrderService

    Given("상품 식별자와 재고를 입력한 경우") {
        val orderRepository = FakeOrderRepository()
        val productRepository = FakeProductRepository()

        sut = OrderService(orderRepository, productRepository)
        productRepository.save(createProduct(1L, "상품1", 1_000))
        productRepository.save(createProduct(2L, "상품2", 2_000))
        val command =
            OrderCreateCommand(
                userId = 100L,
                products =
                    listOf(
                        ProductQuantity(
                            productId = 1L,
                            quantity = 2,
                        ),
                        ProductQuantity(
                            productId = 2L,
                            quantity = 2,
                        ),
                    ),
            )

        When("주문을 생성하면") {
            val order = sut.create(command)

            Then("가격x수량 주문금액으로 주문이 생성된다") {
                order.amount shouldBe 6_000
            }
        }
    }
})
