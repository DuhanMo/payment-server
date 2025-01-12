package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.product

import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.duhan.webfluxcoroutinepaymentserver.core.product.model.Product
import org.duhan.webfluxcoroutinepaymentserver.core.product.port.ProductRepository
import org.springframework.stereotype.Repository

@Repository
class ProductRepositoryImpl(
    private val productCrudRepository: ProductCrudRepository,
) : ProductRepository {
    override suspend fun save(product: Product): Product = productCrudRepository.save(ProductEntity(product)).toModel()

    override suspend fun findAll(): List<Product> = productCrudRepository.findAll().map { it.toModel() }.toList()

    override suspend fun findAllById(productIds: List<Long>): List<Product> =
        productCrudRepository.findAllById(productIds).map { it.toModel() }.toList()
}
