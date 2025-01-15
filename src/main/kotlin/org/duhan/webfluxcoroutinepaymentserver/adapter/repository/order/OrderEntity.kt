package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.duhan.webfluxcoroutinepaymentserver.core.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.PgStatus
import org.duhan.webfluxcoroutinepaymentserver.core.order.model.TossPaymentType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table("orders")
class OrderEntity(
    @Id
    val id: Long?,
    val userId: Long,
    val amount: Long,
    val description: String,
    val pgOrderId: String?,
    val pgKey: String?,
    val pgStatus: PgStatus,
    val pgRetryCount: Int,
    val pgPaymentType: TossPaymentType?,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    constructor(order: Order) : this(
        id = order.id,
        userId = order.userId,
        amount = order.amount,
        description = order.description,
        pgOrderId = order.pgOrderId,
        pgKey = order.pgKey,
        pgStatus = order.pgStatus,
        pgRetryCount = order.pgRetryCount,
        pgPaymentType = order.pgPaymentType,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
    )

    fun toModel(): Order =
        Order(
            id = id,
            userId = userId,
            description = description,
            amount = amount,
            pgOrderId = pgOrderId,
            pgKey = pgKey,
            pgStatus = pgStatus,
            pgRetryCount = pgRetryCount,
            pgPaymentType = pgPaymentType,
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
