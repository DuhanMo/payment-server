package org.duhan.webfluxcoroutinepaymentserver.domain.product.port

import org.duhan.webfluxcoroutinepaymentserver.domain.product.model.Product

interface ProductRepository {
    suspend fun save(product: Product): Product

    suspend fun findAllById(productIds: List<Long>): List<Product>
}
