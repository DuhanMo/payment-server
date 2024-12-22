package org.duhan.webfluxcoroutinepaymentserver.domain.order.model

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus.CREATE
import java.time.LocalDateTime

data class Order(
    val id: Long? = null,
    val userId: Long,
    val amount: Long,
    val description: String,
    val pgOrderId: String? = null,
    val pgKey: String? = null,
    val pgStatus: PgStatus = CREATE,
    val pgRetryCount: Int = 0,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
