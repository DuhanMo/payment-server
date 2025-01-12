package org.duhan.webfluxcoroutinepaymentserver.core.order.service

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.TossPaymentType

data class OrderConfirmCommand(
    val paymentType: TossPaymentType,
    val pgOrderId: String,
    val amount: Long,
    val pgKey: String,
)
