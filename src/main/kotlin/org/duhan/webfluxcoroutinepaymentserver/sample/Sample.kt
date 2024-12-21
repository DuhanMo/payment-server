package org.duhan.webfluxcoroutinepaymentserver.sample

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("sample")
class Sample(
    @Id
    val id: Long? = null,
    val name: String,
)
