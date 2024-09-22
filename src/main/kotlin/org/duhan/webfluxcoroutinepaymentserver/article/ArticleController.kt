package org.duhan.webfluxcoroutinepaymentserver.article

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val service: ArticleService,
) {
    @GetMapping
    suspend fun findAll(request: QueryArticle): Flow<Article> {
        return service.findAll(request)
    }
}
