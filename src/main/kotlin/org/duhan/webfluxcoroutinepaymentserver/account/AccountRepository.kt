package org.duhan.webfluxcoroutinepaymentserver.account

import org.springframework.data.relational.core.sql.LockMode
import org.springframework.data.relational.repository.Lock
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface AccountRepository : CoroutineCrudRepository<Account, Long> {
    @Lock(LockMode.PESSIMISTIC_WRITE)
    suspend fun findAccountById(id: Long): Account?
}
