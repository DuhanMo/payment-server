package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.product

import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface ProductCrudRepository : CoroutineCrudRepository<ProductEntity, Long>
