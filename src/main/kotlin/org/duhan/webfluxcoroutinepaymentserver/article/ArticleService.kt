package org.duhan.webfluxcoroutinepaymentserver.article

import kotlinx.coroutines.flow.Flow
import org.duhan.webfluxcoroutinepaymentserver.common.extension.toLocalDate
import org.duhan.webfluxcoroutinepaymentserver.common.validator.DateString
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.flow
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ArticleService(
    private val dbClient: DatabaseClient,
) {
    suspend fun findAll(request: QueryArticle): Flow<Article> {
        val params = HashMap<String, Any>()
        var sql =
            dbClient.sql(
                """
                SELECT id, title, body, author_id, created_at, updated_at
                FROM articles
                WHERE 1=1
                ${
                    request.title.query {
                        params["title"] = it.trim().let { "%$it%" }
                        "AND title LIKE :title"
                    }
                }
                ${
                    request.authorIds.query {
                        params["authorIds"] = it
                        "AND author_id IN (:authorIds)"
                    }
                }
                ${
                    request.from.query {
                        params["from"] = it.toLocalDate()
                        "AND created_at >= :from"
                    }
                }
                ${
                    request.to.query {
                        params["to"] = it.toLocalDate().plusDays(1)
                        // 2024-01-20 -> 2024-01-21 00:00:00.000
                        // <= -> <
                        "AND created_at < :to"
                    }
                }
                """.trimIndent(),
            )
        params.forEach { (key, value) -> sql = sql.bind(key, value) }
        return sql.map { row ->
            Article(
                id = row.get("id") as Long,
                title = row.get("title") as String,
                body = row.get("body") as String?,
                authorId = row.get("author_id") as Long,
            ).apply {
                createdAt = row.get("created_at") as LocalDateTime
                updatedAt = row.get("created_at") as LocalDateTime
            }
        }.flow()
    }
}

fun <T> T?.query(f: (T) -> String): String {
    return when {
        this == null -> ""
        this is String && this.isBlank() -> ""
        this is Collection<*> && this.isEmpty() -> ""
        this is Array<*> && this.isEmpty() -> ""
        else -> f.invoke(this)
    }
}

data class QueryArticle(
    val title: String?,
    val authorIds: List<Long>?,
    @DateString
    val from: String?,
    @DateString
    val to: String?,
)
