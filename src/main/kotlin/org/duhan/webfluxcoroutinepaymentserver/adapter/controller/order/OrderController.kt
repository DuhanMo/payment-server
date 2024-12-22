package org.duhan.webfluxcoroutinepaymentserver.adapter.controller.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/orders")
class OrderController(
    private val orderService: OrderService,
) {
    @PostMapping
    suspend fun create(
        @RequestBody request: OrderCreateRequest,
    ): Order = orderService.create(request.toCommand())
}
