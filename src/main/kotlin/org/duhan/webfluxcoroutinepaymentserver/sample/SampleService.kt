package org.duhan.webfluxcoroutinepaymentserver.sample

import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class SampleService(
    private val sampleRepository: SampleRepository,
) {
    suspend fun sample(): Flow<Sample> {
        logger.debug { "SampleService.sample" }
        return sampleRepository.findAll()
    }
}
