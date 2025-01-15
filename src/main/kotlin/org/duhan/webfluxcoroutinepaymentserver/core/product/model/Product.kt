package org.duhan.webfluxcoroutinepaymentserver.core.product.model

import java.time.Instant

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
)
