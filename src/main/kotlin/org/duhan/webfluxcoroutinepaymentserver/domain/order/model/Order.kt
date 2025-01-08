package org.duhan.webfluxcoroutinepaymentserver.domain.order.model

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.AUTH_INVALID
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.AUTH_SUCCESS
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CAPTURE_FAIL
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CAPTURE_REQUEST
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CAPTURE_RETRY
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CAPTURE_SUCCESS
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CREATE
import org.duhan.webfluxcoroutinepaymentserver.domain.order.service.OrderConfirmCommand
import java.time.LocalDateTime

class Order(
    val id: Long? = null,
    val userId: Long,
    val amount: Long,
    val description: String,
    val pgOrderId: String? = null,
    var pgKey: String? = null,
    var pgStatus: PgStatus = CREATE,
    val pgRetryCount: Int = 0,
    var pgPaymentType: TossPaymentType? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun payAuth(command: OrderConfirmCommand) {
        pgPaymentType = command.paymentType
        pgKey = command.pgKey
        if (amount == command.amount) {
            pgStatus = AUTH_SUCCESS
        } else {
            pgStatus = AUTH_INVALID
        }
    }

    fun captureRequest() {
        check(pgStatus == AUTH_SUCCESS) { "AUTH_SUCCESS 에서 결제승인 시도 가능합니다" }
        pgStatus = CAPTURE_REQUEST
    }

    fun captureSuccess() {
        check(pgStatus == CAPTURE_REQUEST) { "CAPTURE_REQUEST 에서 결제승인 가능합니다" }
        pgStatus = CAPTURE_SUCCESS
    }

    fun captureFail() {
        check(pgStatus == CAPTURE_REQUEST) { "CAPTURE_REQUEST 에서 결제실패 가능합니다" }
        pgStatus = CAPTURE_FAIL
    }

    fun captureRetry() {
        check(pgStatus == CAPTURE_REQUEST) { "CAPTURE_REQUEST 에서 결제 재시도 필요상태 가능합니다" }
        pgStatus = CAPTURE_RETRY
    }
}
