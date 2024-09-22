package org.duhan.webfluxcoroutinepaymentserver.sample

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.test.context.ActiveProfiles

private const val KEY = "key"

@SpringBootTest
@ActiveProfiles("test")
class RedisTemplateTest(
    private val template: ReactiveRedisTemplate<Any, Any>,
) : StringSpec({
        afterTest {
            template.delete(KEY).awaitSingle()
        }

        "reactive redis test" {
            val ops = template.opsForValue()
            ops.set(KEY, "value 1").awaitSingle()
            ops.get(KEY).awaitSingle() shouldBe "value 1"
        }
    })
