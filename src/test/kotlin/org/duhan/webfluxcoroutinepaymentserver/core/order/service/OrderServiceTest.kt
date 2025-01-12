package org.duhan.webfluxcoroutinepaymentserver.core.order.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.collections.shouldContainAll
import io.kotest.matchers.shouldBe
import org.duhan.webfluxcoroutinepaymentserver.core.order.mock.FakeOrderProductRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.mock.FakeOrderRepository
import org.duhan.webfluxcoroutinepaymentserver.core.order.service.OrderCreateCommand.ProductQuantityCommand
import org.duhan.webfluxcoroutinepaymentserver.core.product.FakeProductRepository
import org.duhan.webfluxcoroutinepaymentserver.fixture.createProduct

class OrderServiceTest : BehaviorSpec({
    lateinit var sut: OrderService
    lateinit var orderRepository: FakeOrderRepository
    lateinit var productRepository: FakeProductRepository
    lateinit var orderProductRepository: FakeOrderProductRepository

    fun setup() {
        orderRepository = FakeOrderRepository()
        productRepository = FakeProductRepository()
        orderProductRepository = FakeOrderProductRepository()
        sut = OrderService(orderRepository, productRepository, orderProductRepository)
    }

    Given("상품 식별자와 재고를 입력한 경우") {
        setup()
        productRepository.save(createProduct(1L, "상품1", 1_000))
        productRepository.save(createProduct(2L, "상품2", 2_000))
        val command =
            OrderCreateCommand(
                userId = 100L,
                products =
                    listOf(
                        ProductQuantityCommand(
                            productId = 1L,
                            quantity = 2,
                        ),
                        ProductQuantityCommand(
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

            Then("주문상품이 생성된다") {
                val orderProducts = orderProductRepository.findAll()
                orderProducts.forAll { it.orderId shouldBe order.id }
                orderProducts.map { it.productId } shouldContainAll listOf(1L, 2L)
            }
        }
    }

    Given("존재하지 않는 상품 식별자인 경우") {
        setup()
        val command =
            OrderCreateCommand(
                userId = 100L,
                products =
                    listOf(
                        ProductQuantityCommand(
                            productId = 1L,
                            quantity = 2,
                        ),
                        ProductQuantityCommand(
                            productId = 2L,
                            quantity = 2,
                        ),
                    ),
            )

        When("주문을 생성하면") {
            Then("예외 발생한다") {
                shouldThrow<IllegalArgumentException> { sut.create(command) }
            }
        }
    }
})
