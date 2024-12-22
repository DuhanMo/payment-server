package org.duhan.webfluxcoroutinepaymentserver.domain.product.model

import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
