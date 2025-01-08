package org.duhan.webfluxcoroutinepaymentserver.adapter.client

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.TossPaymentType
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderConfirmCommand

data class TossPayConfirmRequest(
    val paymentType: TossPaymentType,
    val paymentKey: String,
    val orderId: String,
    val amount: Long,
) {
    companion object {
        fun of(command: OrderConfirmCommand): TossPayConfirmRequest =
            TossPayConfirmRequest(
                command.paymentType,
                command.pgKey,
                command.pgOrderId,
                command.amount,
            )
    }
}
