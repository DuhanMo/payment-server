package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldBe
import org.duhan.webfluxcoroutinepaymentserver.fixture.createOrderProduct
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestConstructor

@ActiveProfiles("test")
@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderProductRepositoryImplTest(
    val orderProductRepository: OrderProductRepositoryImpl,
) : ExpectSpec({
        extension(SpringExtension)

        context("주문상품 저장") {
            expect("주문상품을 저장한다") {
                val actual = orderProductRepository.save(createOrderProduct(orderId = 1L, productId = 1L))

                actual.id shouldBe 1L
                actual.price shouldBe 500
            }
        }

        context("주문상품 조회") {
            orderProductRepository.save(createOrderProduct(orderId = 1L, productId = 2L))
            orderProductRepository.save(createOrderProduct(orderId = 1L, productId = 3L))

            expect("주문상품을 모두 조회한다") {
                orderProductRepository.findAll().size shouldBe 2
            }
        }
    })
