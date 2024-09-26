package org.duhan.webfluxcoroutinepaymentserver.account

import kotlinx.coroutines.delay
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService(
    private val repository: AccountRepository,
) {
    suspend fun find(id: Long): Account {
        return repository.findById(id) ?: throw NoSuchElementException()
    }

    @Transactional
    suspend fun deposit(
        id: Long,
        request: AccountUpdateRequest,
    ) {
//        val account = repository.findById(id) ?: throw NoSuchElementException()
//        repository.save(account.apply {
//            delay(3000)
//            amount += request.amount
//        })
    }

    @Transactional
    suspend fun depositPessimisticLock(
        id: Long,
        request: AccountUpdateRequest,
    ) {
        val account = repository.findAccountById(id) ?: throw NoSuchElementException()
        repository.save(
            account.apply {
                delay(1000)
                amount += request.amount
            },
        )
    }
}

data class AccountUpdateRequest(
    val amount: Long,
)
