package org.duhan.webfluxcoroutinepaymentserver.domain.order.model

enum class PgStatus {
    CREATE,
    AUTH_SUCCESS,
    AUTH_FAIL,
    AUTH_INVALID,
    CAPTURE_REQUEST,
    CAPTURE_RETRY,
    CAPTURE_SUCCESS,
    CAPTURE_FAIL,
}
