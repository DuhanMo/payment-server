package org.duhan.webfluxcoroutinepaymentserver.common.validator

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import org.duhan.webfluxcoroutinepaymentserver.common.extension.toLocalDate
import org.duhan.webfluxcoroutinepaymentserver.common.extension.toString
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = [DateValidator::class])
annotation class DateString(
    val message: String = "not a valid date",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
)

class DateValidator : ConstraintValidator<DateString, String> {
    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext?,
    ): Boolean {
        // 20240919 -> yyyyMMdd
        val text = value?.filter { it.isDigit() } ?: return true
        return runCatching {
            text.toLocalDate().let {
                if (text != it.toString("yyyyMMdd")) null else true
            }
        }.getOrNull() != null
    }
}
