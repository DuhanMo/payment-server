package org.duhan.webfluxcoroutinepaymentserver.article

import kotlinx.coroutines.flow.Flow
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
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

    @GetMapping("/{id}")
    suspend fun find(
        @PathVariable id: Long,
    ): Article {
        return service.find(id)
    }

    @PutMapping("/{id}")
    suspend fun update(
        @PathVariable id: Long,
        @RequestBody request: ReqUpdate,
    ): Article {
        return service.update(id, request)
    }

    @DeleteMapping("/{id}")
    suspend fun delete(
        @PathVariable id: Long,
    ) {
        return service.delete(id)
    }
}
