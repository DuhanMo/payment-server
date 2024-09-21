package org.duhan.webfluxcoroutinepaymentserver.sample

import io.github.resilience4j.circuitbreaker.CallNotPermittedException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.kotlin.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.kotlin.circuitbreaker.executeSuspendFunction
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private val logger = KotlinLogging.logger {}

@Service
class ExternalApi(
    @Value("\${api.externalUrl}")
    private val externalUrl: String,
) {
    private val circuitBreaker =
        CircuitBreaker.of(
            "test",
            CircuitBreakerConfig {
                slidingWindowSize(10)
                failureRateThreshold(20.0F)
                // Open 상태로 전환된 후 일정 시간이 지나면 Half-Open 상태로 전환됨.
                waitDurationInOpenState(10.seconds.toJavaDuration())
                // Half-Open 상태에서 허용할 요청의 수를 설정.
                permittedNumberOfCallsInHalfOpenState(3)
            },
        )

    private val client =
        WebClient.builder().baseUrl(externalUrl)
            .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .build()

    suspend fun testCircuitBreaker(error: String): String {
        logger.debug { "1. request call" }
        return try {
            circuitBreaker.executeSuspendFunction {
                logger.debug { "2. call external" }
                client.get().uri("/dummy?error=$error").retrieve().awaitBody()
            }
        } catch (e: CallNotPermittedException) {
            return "call blocked by circuit breaker"
        } finally {
            logger.debug { "3. done" }
        }
    }
}
