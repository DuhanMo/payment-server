package org.duhan.webfluxcoroutinepaymentserver.sample

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
class SampleController(
    private val sampleService: SampleService,
) {
    @GetMapping("/test/samples")
    suspend fun sample(): Flow<Sample> {
        logger.debug { "SampleController.sample" }
        delay(100)
        return sampleService.sample()
    }
}
