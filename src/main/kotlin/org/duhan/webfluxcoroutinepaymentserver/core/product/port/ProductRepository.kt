package org.duhan.webfluxcoroutinepaymentserver.core.product.port

import org.duhan.webfluxcoroutinepaymentserver.core.product.model.Product

interface ProductRepository {
    suspend fun save(product: Product): Product

    suspend fun findAll(): List<Product>

    suspend fun findAllById(ids: List<Long>): List<Product>
}
