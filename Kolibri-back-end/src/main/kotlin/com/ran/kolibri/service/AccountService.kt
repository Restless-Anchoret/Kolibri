package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.ExpendOperation
import com.ran.kolibri.entity.financial.IncomeOperation
import com.ran.kolibri.entity.financial.TransferOperation
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.extension.logDebug
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.financial.AccountSpecificationFactory
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class AccountService {

    companion object {
        private val LOGGER = Logger.getLogger(AccountService::class.java)
    }

    @Autowired
    lateinit var financialProjectService: FinancialProjectService
    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Transactional
    fun getAccountsByProjectId(projectId: Long, name: String? = null, pageable: Pageable? = null): Page<Account> {
        financialProjectService.getFinancialProjectById(projectId)
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
    fun createAccount(projectId: Long, name: String, description: String): Account {
        val project = financialProjectService.getFinancialProjectById(projectId)
        val account = Account(name, description)
        account.project = project
        accountRepository.save(account)
        return account
    }

    @Transactional
    fun editAccount(accountId: Long, name: String, description: String, isActive: Boolean): Account {
        val account = getAccountById(accountId)
        account.name = name
        account.description = description
        account.isActive = isActive
        accountRepository.save(account)
        return account
    }

    @Transactional
    fun removeAccount(accountId: Long) {
        val account = getAccountById(accountId)
        val operationsPage = accountRepository.findAllAccountOperations(accountId)
        if (!operationsPage.content.isEmpty()) {
            throw BadRequestException("Account cannot be removed, because it was used in Operations")
        }
        accountRepository.delete(account)
    }

    @Transactional
    fun changeAccountMoney(accountId: Long, accountMoneyDelta: Double): Account {
        LOGGER.logInfo { "Changing Account money: accountId: $accountId, accountMoneyDelta = $accountMoneyDelta" }
        val account = getAccountById(accountId)
        if (account.currentMoneyAmount + accountMoneyDelta < -1e-8) {
            throw BadRequestException("Not enough money on Account '${account.name}'")
        }

        account.currentMoneyAmount += accountMoneyDelta
        LOGGER.logDebug { "New Account money amount: ${account.currentMoneyAmount}" }

        accountRepository.save(account)
        LOGGER.logInfo { "New Account money amount was saved" }
        return account
    }

    @Transactional
    fun findAccountMoneyAmountForDate(accountId: Long, date: Date): Double {
        LOGGER.logInfo { "Finding Account money amount for date: accountId = $accountId, date: $date" }
        val operationsPage = accountRepository.findAccountOperationsBeforeDate(accountId, date)

        if (operationsPage.content.isEmpty()) {
            LOGGER.logInfo { "Last Account Operation was not found, returning zero" }
            return 0.0
        }

        val operation = operationsPage.content[0]
        LOGGER.logDebug { "Last Account Operation = $operation" }

        val moneyAmount = when (operation.javaClass) {
            IncomeOperation::class.java -> {
                (operation as IncomeOperation).resultMoneyAmount
            }
            ExpendOperation::class.java -> {
                (operation as ExpendOperation).resultMoneyAmount
            }
            TransferOperation::class.java -> {
                operation as TransferOperation
                if (operation.fromAccount?.id == accountId) {
                    operation.fromAccountResultMoneyAmount
                } else {
                    operation.toAccountResultMoneyAmount
                }
            }
            else -> 0.0
        }

        LOGGER.logInfo { "Found money amount: $moneyAmount" }
        return moneyAmount
    }

    @Transactional
    fun addAccountComment(accountId: Long, commentText: String) {
        val operation = getAccountById(accountId)
        commentService.addComment(operation, accountRepository, commentText)
    }

    @Transactional
    fun editAccountComment(accountId: Long, commentIndex: Int, commentText: String) {
        val operation = getAccountById(accountId)
        commentService.editComment(operation, commentIndex, commentText)
    }

    @Transactional
    fun removeAccountComment(accountId: Long, commentIndex: Int) {
        val operation = getAccountById(accountId)
        commentService.removeComment(operation, accountRepository, commentIndex)
    }

}
