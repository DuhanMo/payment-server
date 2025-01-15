package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.product

import org.duhan.webfluxcoroutinepaymentserver.core.product.model.Product
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("product")
class ProductEntity(
    @Id
    val id: Long?,
    val name: String,
    val price: Long,
    val createdAt: Instant,
    val updatedAt: Instant,
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
