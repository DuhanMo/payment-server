package org.duhan.webfluxcoroutinepaymentserver.adapter.repository.order

import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.Order
import org.duhan.webfluxcoroutinepaymentserver.domain.order.model.PgStatus
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("order")
class OrderEntity(
    val id: Long?,
    val userId: Long,
    val amount: Int,
    val description: String,
    val pgOrderId: String?,
    val pgKey: String?,
    val pgStatus: PgStatus,
    val pgRetryCount: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
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
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
}
