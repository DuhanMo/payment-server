package org.duhan.webfluxcoroutinepaymentserver.sample

import jakarta.validation.Valid
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.Size
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import mu.KotlinLogging
import org.duhan.webfluxcoroutinepaymentserver.common.validator.DateString
import org.duhan.webfluxcoroutinepaymentserver.exception.InvalidParameter
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @PostMapping("/test/error")
    suspend fun error(
        @RequestBody @Valid request: ReqErrorTest,
    ) {
        if (request.message == "error") {
            throw InvalidParameter(request, request::message, code = "custom code", message = "custom error")
        }
    }
}

data class ReqErrorTest(
    @field:NotEmpty
    @field:Size(min = 3, max = 10)
    val id: String?,
    @field:NotNull
    @field:Positive(message = "양수만 입력 가능")
    @field:Max(100, message = "최대 100까지 입력가능합니다.")
    val age: Int?,
    @field:DateString
    val birthday: String?,
    val message: String? = null,
)
