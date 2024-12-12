package org.duhan.webfluxcoroutinepaymentserver.sample

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SampleService(
    private val repository: SampleRepository,
) {
    suspend fun findAll(): Flow<Sample> = repository.findAll()

    @Transactional
    suspend fun create(name: String) {
        repository.save(
            Sample(
                name = name
            )
        )
    }
}