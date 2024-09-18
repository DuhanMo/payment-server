package org.duhan.webfluxcoroutinepaymentserver.common.extension

import org.springframework.http.server.reactive.ServerHttpRequest
import java.util.concurrent.ConcurrentHashMap

private val mapReqIdToTxId = ConcurrentHashMap<String, String>()

var ServerHttpRequest.txid: String?
    get() = mapReqIdToTxId[id]
    set(value) {
        if (value == null) {
            mapReqIdToTxId.remove(id)
        } else {
            mapReqIdToTxId[id] = value
        }
    }
