package org.duhan.webfluxcoroutinepaymentserver.sample

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sample")
class SampleController(
    private val service: SampleService,
) {
    @GetMapping
    suspend fun hello(): Flow<Sample> = service.findAll()

    @PostMapping
    suspend fun create(
        @RequestBody request: CreateRequest,
    ) = service.create(request.name)
}

data class CreateRequest(
    val name: String,
)
