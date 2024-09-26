package org.duhan.webfluxcoroutinepaymentserver.account

import org.duhan.webfluxcoroutinepaymentserver.article.BaseEntity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table

@Table("accounts")
class Account(
    @Id
    val id: Long? = null,
    var amount: Long,
    @Version
    var version: Long = 1,
) : BaseEntity()
