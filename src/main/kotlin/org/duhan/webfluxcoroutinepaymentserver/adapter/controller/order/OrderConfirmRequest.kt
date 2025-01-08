package org.duhan.webfluxcoroutinepaymentserver.adapter.controller.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.TossPaymentType
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderConfirmCommand

data class OrderConfirmRequest(
    val paymentType: TossPaymentType,
    val orderId: String,
    val amount: String,
    val paymentKey: String,
) {
    fun toCommand(): OrderConfirmCommand = OrderConfirmCommand(paymentType, orderId, amount.toLong(), paymentKey)
}
