package org.duhan.webfluxcoroutinepaymentserver.account

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/accounts")
class AccountController(
    private val service: AccountService,
) {
    @GetMapping("/{id}")
    suspend fun find(
        @PathVariable("id") id: Long,
    ): Account {
        return service.find(id)
    }

    @PutMapping("/deposit/{id}")
    suspend fun deposit(
        @PathVariable("id") id: Long,
        @RequestBody request: AccountUpdateRequest,
    ): Account {
        service.deposit(id, request)
        return service.find(id)
    }

    @PutMapping("/deposit2/{id}")
    suspend fun deposit2(
        @PathVariable("id") id: Long,
        @RequestBody request: AccountUpdateRequest,
    ): Account {
        service.depositPessimisticLock(id, request)
        return service.find(id)
    }
}
