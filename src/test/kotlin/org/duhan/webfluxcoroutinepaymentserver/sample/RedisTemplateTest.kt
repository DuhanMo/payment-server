package org.duhan.webfluxcoroutinepaymentserver.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles
import java.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

private const val KEY = "key"

@SpringBootTest
@ActiveProfiles("test")
class RedisTemplateTest(
    private val template: ReactiveRedisTemplate<Any, Any>,
) : StringSpec({
        suspend fun waitForKeyExpiration(
            key: String,
            template: ReactiveRedisTemplate<Any, Any>,
            timeout: Duration,
        ) {
            val start = System.currentTimeMillis()
            while (System.currentTimeMillis() - start < timeout.toMillis()) {
                try {
                    template.opsForValue().get(key).awaitSingle()
                } catch (e: NoSuchElementException) {
                    return
                }
                delay(100) // 100ms 단위로 확인
            }
            throw AssertionError("Key did not expire in the expected time")
        }
        afterTest {
            template.delete(KEY).awaitSingle()
        }

        "reactive redis test" {
            val ops = template.opsForValue()
            shouldThrow<NoSuchElementException> {
                ops.get(KEY).awaitSingle()
            }
            ops.set(KEY, "value 1").awaitSingle()
            ops.get(KEY).awaitSingle() shouldBe "value 1"

            template.expire(KEY, 500.milliseconds.toJavaDuration()).awaitSingle()
            waitForKeyExpiration(KEY, template, 2.seconds.toJavaDuration())

            shouldThrow<NoSuchElementException> {
                ops.get(KEY).awaitSingle()
            }
        }
    })
