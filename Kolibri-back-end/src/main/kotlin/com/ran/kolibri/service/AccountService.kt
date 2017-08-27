package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.financial.AccountSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AccountService {

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Transactional
    fun getAccountsByProjectId(projectId: Long, name: String?, pageable: Pageable?): Page<Account> {
        val specification = Specifications.where(AccountSpecificationFactory.byProjectId(projectId))
                .and(BaseSpecificationFactory.byName<Account>(name))
        return accountRepository.findAll(specification, pageable)
    }

    @Transactional
    fun getAccountById(accountId: Long): Account {
        return accountRepository.findOne(accountId) ?:
                throw NotFoundException("Account was not found")
    }

    @Transactional
    fun changeAccountMoney(accountId: Long, accountMoneyDelta: Double): Account {
        val account = getAccountById(accountId)
        if (account.currentMoneyAmount + accountMoneyDelta < 0.0) {
            throw BadRequestException("Not enough money on Account '${account.name}'")
        }
        account.currentMoneyAmount += accountMoneyDelta
        accountRepository.save(account)
        return account
    }

}
