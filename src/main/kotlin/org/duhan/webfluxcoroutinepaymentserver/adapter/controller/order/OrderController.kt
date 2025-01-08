package org.duhan.webfluxcoroutinepaymentserver.adapter.controller.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/orders")
class OrderController(
    private val orderService: OrderService,
) {
    @PostMapping
    suspend fun create(
        @RequestBody request: OrderCreateRequest,
    ): ResponseEntity<Order> = ResponseEntity.ok(orderService.create(request.toCommand()))

    @PostMapping("/pay/auth")
    suspend fun payAuth(
        @RequestBody request: OrderConfirmRequest,
    ): ResponseEntity<Order> = ResponseEntity.ok(orderService.payAuth(request.toCommand()))

    @PostMapping("/pay/confirm")
    suspend fun confirm(
        @RequestBody request: OrderConfirmRequest,
    ): ResponseEntity<Order> = ResponseEntity.ok(orderService.payConfirm(request.toCommand()))
}
