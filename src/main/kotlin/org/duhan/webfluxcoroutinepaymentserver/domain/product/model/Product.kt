package org.duhan.webfluxcoroutinepaymentserver.domain.product.model

import java.time.LocalDateTime

data class Product(
    val id: Long? = null,
    val name: String,
    val price: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
