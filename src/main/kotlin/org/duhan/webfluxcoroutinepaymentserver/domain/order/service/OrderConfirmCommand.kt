package org.duhan.webfluxcoroutinepaymentserver.domain.order.service

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.TossPaymentType

data class OrderConfirmCommand(
    val paymentType: TossPaymentType,
    val pgOrderId: String,
    val amount: Long,
    val pgKey: String,
)
