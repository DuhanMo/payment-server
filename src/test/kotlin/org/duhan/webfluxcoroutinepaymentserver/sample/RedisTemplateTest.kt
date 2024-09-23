package org.duhan.webfluxcoroutinepaymentserver.sample

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import mu.KotlinLogging
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Range
import org.springframework.data.geo.Circle
import org.springframework.data.geo.Distance
import org.springframework.data.geo.Metrics
import org.springframework.data.geo.Point
import org.springframework.data.redis.connection.DataType
import org.springframework.data.redis.connection.RedisGeoCommands.GeoLocation
import org.springframework.data.redis.core.ReactiveListOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.core.ReactiveZSetOperations
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.GenericContainer
import org.testcontainers.utility.DockerImageName
import java.time.Duration
import java.util.Date
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

const val KEY = "key"
private val logger = KotlinLogging.logger {}

@SpringBootTest
@ActiveProfiles("test")
class RedisTemplateTest(
    private val template: ReactiveRedisTemplate<Any, Any>,
) : WithRedisContainer, StringSpec({

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

        "LinkedList" {
            val ops = template.opsForList()
            ops.rightPushAll(KEY, 2, 3, 4, 5).awaitSingle()

            template.type(KEY).awaitSingle() shouldBe DataType.LIST
            ops.size(KEY).awaitSingle() shouldBe 4
            ops.all(KEY) shouldBe listOf(2, 3, 4, 5)

            ops.rightPush(KEY, 6).awaitSingle()
            ops.all(KEY) shouldBe listOf(2, 3, 4, 5, 6)

            ops.leftPop(KEY).awaitSingle() shouldBe 2
            ops.all(KEY) shouldBe listOf(3, 4, 5, 6)

            ops.leftPush(KEY, 9).awaitSingle()
            ops.all(KEY) shouldBe listOf(9, 3, 4, 5, 6)
            ops.rightPop(KEY).awaitSingle() shouldBe 6
            ops.all(KEY) shouldBe listOf(9, 3, 4, 5)
        }

        "LinkedList LRU" {
            val ops = template.opsForList()
            ops.rightPushAll(KEY, 7, 6, 4, 3, 2, 1, 3).awaitSingle()

            ops.remove(KEY, 0, 2).awaitSingle()
            ops.all(KEY) shouldBe listOf(7, 6, 4, 3, 1, 3)

            ops.leftPush(KEY, 2).awaitSingle()
            ops.all(KEY) shouldBe listOf(2, 7, 6, 4, 3, 1, 3)
        }

        "Hash" {
            val ops = template.opsForHash<Int, String>()
            val map = (1..10).associateWith { "value-$it" }
            ops.putAll(KEY, map).awaitSingle()

            ops.size(KEY).awaitSingle() shouldBe 10
            ops.get(KEY, 1).awaitSingle() shouldBe "value-1"
            ops.get(KEY, 8).awaitSingle() shouldBe "value-8"
        }

        // 실시간 랭킹 등 처리.
        "Sorted Set" {
            val ops = template.opsForZSet()
            listOf(8, 9, 11, 4, 6, 32, 1, 8).forEach {
                ops.add(KEY, it, -1.0 * Date().time).awaitSingle()
                // ops.all(KEY).let { logger.debug { it } }
            }
            template.delete(KEY).awaitSingle()

            listOf(
                "jake" to 123,
                "chulsoo" to 752,
                "yeonghee" to 932,
                "john" to 335,
                "jake" to 623,
            ).also {
                it.toMap().toList().sortedBy { it.second }.let { logger.debug { "original: $it" } }
            }.forEach {
                ops.add(KEY, it.first, it.second * -1.0).awaitSingle()
                ops.all(KEY).let { logger.debug { it } }
            }
        }

        // 지역 관련 데이터 처리.
        "Geo Redis" {
            val ops = template.opsForGeo()
            listOf(
                GeoLocation("seoul", Point(126.97806, 37.56667)),
                GeoLocation("busan", Point(129.07556, 35.17944)),
                GeoLocation("incheon", Point(126.70528, 37.45639)),
                GeoLocation("daegu", Point(128.60250, 35.87222)),
                GeoLocation("anyang", Point(126.95556, 37.39444)),
                GeoLocation("daejeon", Point(127.38500, 36.35111)),
                GeoLocation("gwangju", Point(126.85306, 35.15972)),
                GeoLocation("suwon", Point(127.02861, 37.26389)),
            ).forEach {
                ops.add(KEY, it as GeoLocation<Any>).awaitSingle()
            }

            ops.distance(KEY, "seoul", "busan").awaitSingle()
                .let { logger.debug { "seoul -> busan: $it" } }

            val point = ops.position(KEY, "daegu").awaitSingle().also { logger.debug { it } }
            val circle = Circle(point, Distance(200.0, Metrics.KILOMETERS))

            ops.radius(KEY, circle).asFlow().map { it.content.name }.toList().let {
                logger.debug { "cities near daegu: $it" }
            }
        }

        // 페이지 기록등 대용량 로그 기록.
        "Hyper loglog" {
            val ops = template.opsForHyperLogLog()
            ops.add("page1", "192.179.0.23", "41.61.2.230", "225.105.161.131").awaitSingle()
            ops.add("page2", "1.1.1.1", "2.2.2.2").awaitSingle()
            ops.add("page3", "9.9.9.9").awaitSingle()
            ops.add("page3", "8.8.8.8").awaitSingle()
            ops.add("page3", "7.7.7.7", "2.2.2.2", "1.1.1.1").awaitSingle()
            ops.size("page3").awaitSingle() shouldBe 5L
        }

        "Pub/Sub" {
            template.listenToChannel("channel-1").doOnNext {
                logger.debug { ">> received 1: ${it.message}" }
            }.subscribe()

            template.listenToChannel("channel-1").doOnNext {
                logger.debug { ">> received 2: ${it.message}" }
            }.subscribe()

            template.listenToChannel("channel-1").asFlow().onEach {
                logger.debug { ">> received 3: ${it.message}" }
            }.launchIn(CoroutineScope(Dispatchers.IO))

            repeat(10) {
                val message = "test message (${it + 1})"
                logger.debug { ">> send: $message" }
                template.convertAndSend("channel-1", message).awaitSingle()
                delay(100)
            }
        }
    })

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

suspend fun ReactiveListOperations<Any, Any>.all(key: Any): List<Any> {
    return this.range(key, 0, -1).asFlow().toList()
}

suspend fun ReactiveZSetOperations<Any, Any>.all(key: Any): List<Any> {
    return this.range(key, Range.closed(0, -1)).asFlow().toList()
}

interface WithRedisContainer {
    companion object {
        private val container =
            GenericContainer(DockerImageName.parse("redis")).apply {
                addExposedPorts(6379)
                start()
            }

        @DynamicPropertySource
        @JvmStatic
        fun setProperty(registry: DynamicPropertyRegistry) {
            logger.debug { "redis mapped port: ${container.getMappedPort(6379)}" }
            registry.add("spring.data.redis.port") {
                "${container.getMappedPort(6379)}"
            }
        }
    }
}
