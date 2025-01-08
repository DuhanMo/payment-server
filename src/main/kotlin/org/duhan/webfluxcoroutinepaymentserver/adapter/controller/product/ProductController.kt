package org.duhan.webfluxcoroutinepaymentserver.adapter.controller.product

import org.duhan.webfluxcoroutinepaymentserver.domain.product.model.Product
import org.duhan.webfluxcoroutinepaymentserver.domain.product.service.ProductQueryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/products")
class ProductController(
    private val productQueryService: ProductQueryService,
) {
    @GetMapping
    suspend fun findAll(): ResponseEntity<List<Product>> = ResponseEntity.ok(productQueryService.findAll())
}
