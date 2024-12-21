package org.duhan.webfluxcoroutinepaymentserver.fixture

import org.duhan.webfluxcoroutinepaymentserver.domain.product.model.Product
import java.time.LocalDateTime.now

fun createProduct(
    id: Long? = null,
    name: String = "상품1",
    price: Int = 100,
) = Product(
    id = id,
    name = name,
    price = price,
    createdAt = now(),
    updatedAt = now(),
)
