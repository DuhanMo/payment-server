package org.duhan.webfluxcoroutinepaymentserver.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

@SpringBootTest
@ActiveProfiles("test")
class TestContainerTest(
    private val template: ReactiveRedisTemplate<Any, Any>,
) : WithRedisContainer, StringSpec({
        "Test Container 공유 실행 테스트" {
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
