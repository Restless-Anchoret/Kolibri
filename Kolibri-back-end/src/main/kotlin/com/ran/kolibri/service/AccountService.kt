package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var accountRepository: AccountRepository

    @Transactional
    fun getAllAccountsByProjectId(projectId: Long): List<Account> {
        return projectService.getFinancialProjectById(projectId).accounts
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