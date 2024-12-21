package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.product

import org.duhan.webfluxcoroutinepaymentserver.domain.product.model.Product
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("order")
class ProductEntity(
    val id: Long?,
    val name: String,
    val price: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    constructor(product: Product) : this(
        id = product.id,
        name = product.name,
        price = product.price,
        createdAt = product.createdAt,
        updatedAt = product.updatedAt,
    )

    fun toModel(): Product =
        Product(
            id = id,
            name = name,
            price = price,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
