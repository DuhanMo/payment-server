package org.duhan.webfluxcoroutinepaymentserver.article

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime

@Table("articles")
class Article(
    @Id
    val id: Long? = null,
    var title: String,
    var body: String?,
    var authorId: Long?,
) : BaseEntity()

open class BaseEntity(
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null,
) : Serializable
