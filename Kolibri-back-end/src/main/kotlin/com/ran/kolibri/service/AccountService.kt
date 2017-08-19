package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Transactional
    fun getAllAccountsByProjectId(projectId: Long, pageable: Pageable): Page<Account> {
        return accountRepository.findByProjectId(projectId, pageable)
    }

    @Transactional
    fun getAccountById(accountId: Long): Account {
        return accountRepository.findOne(accountId) ?:
                throw NotFoundException("Project was not found for id: $accountId")
    }

    @Transactional
    fun changeAccountMoney(accountId: Long, accountMoneyDelta: Double): Account {
        val account = getAccountById(accountId)
        account.currentMoneyAmount += accountMoneyDelta
        accountRepository.save(account)
        return account
    }

}