package org.duhan.webfluxcoroutinepaymentserver.core.product.service

import org.duhan.webfluxcoroutinepaymentserver.core.product.model.Product
import org.duhan.webfluxcoroutinepaymentserver.core.product.port.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductQueryService(
    private val productRepository: ProductRepository,
) {
    @Transactional(readOnly = true)
    suspend fun findAll(): List<Product> {
        return productRepository.findAll()
    }
}
