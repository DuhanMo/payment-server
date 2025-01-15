package org.duhan.webfluxcoroutinepaymentserver.fixture

import org.duhan.webfluxcoroutinepaymentserver.core.product.model.Product
import java.time.Instant.now

fun createProduct(
    id: Long? = null,
    name: String = "상품1",
    price: Long = 100,
) = Product(
    id = id,
    name = name,
    price = price,
    createdAt = now(),
    updatedAt = now(),
)
