package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.*
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.repository.financial.OperationRepository
import com.ran.kolibri.specification.financial.OperationSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OperationService {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationCategoryService: OperationCategoryService

    @Autowired
    lateinit var operationRepository: OperationRepository

    @Transactional
    fun getOperationsByProjectId(projectId: Long, pageable: Pageable?): Page<Operation> {
        val specification = Specifications.where(OperationSpecificationFactory.byProjectId<Operation>(projectId))
        return operationRepository.findAll(specification, pageable)
    }

    @Transactional
    fun addIncomeOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date? = null) {
        val account = accountService.changeAccountMoney(accountId, moneyAmount)
        ensureAccountBelongsToProject(account, projectId)
        val operation = IncomeOperation()
        operation.incomeAccount = account
        operation.resultMoneyAmount = account.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, operationDate)
    }

    @Transactional
    fun addExpendOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date? = null) {
        val account = accountService.changeAccountMoney(accountId, -moneyAmount)
        ensureAccountBelongsToProject(account, projectId)
        val operation = ExpendOperation()
        operation.expendAccount = account
        operation.resultMoneyAmount = account.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, operationDate)
    }

    @Transactional
    fun addTransferOperation(projectId: Long, fromAccountId: Long, toAccountId: Long,
                             operationCategoryId: Long, moneyAmount: Double, comment: String, operationDate: Date? = null) {
        val fromAccount = accountService.changeAccountMoney(fromAccountId, -moneyAmount)
        val toAccount = accountService.changeAccountMoney(toAccountId, moneyAmount)
        ensureAccountBelongsToProject(fromAccount, projectId)
        ensureAccountBelongsToProject(toAccount, projectId)
        val operation = TransferOperation()
        operation.fromAccount = fromAccount
        operation.toAccount = toAccount
        operation.fromAccountResultMoneyAmount = fromAccount.currentMoneyAmount
        operation.toAccountResultMoneyAmount = toAccount.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, operationDate)
    }

    private fun addOperation(operation: Operation, projectId: Long, operationCategoryId: Long,
                             moneyAmount: Double, comment: String, operationDate: Date? = null) {
        val project = projectService.getFinancialProjectById(projectId)
        val operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)
        ensureOperationCategoryBelongsToProject(operationCategory, projectId)
        operation.operationCategory = operationCategory
        operation.moneyAmount = moneyAmount
        operation.comment = comment
        operation.operationDate = operationDate ?: Date()
        operation.project = project
        operationRepository.save(operation)
    }

    private fun ensureOperationCategoryBelongsToProject(operationCategory: OperationCategory, projectId: Long) {
        if (operationCategory.project?.id != projectId) {
            throw BadRequestException("Operation Category does not belong to Project")
        }
    }

    private fun ensureAccountBelongsToProject(account: Account, projectId: Long) {
        if (account.project?.id != projectId) {
            throw BadRequestException("Account does not belong to Project")
        }
    }

}
