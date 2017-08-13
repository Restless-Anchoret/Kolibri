package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.ExpendOperation
import com.ran.kolibri.entity.financial.IncomeOperation
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.financial.TransferOperation
import com.ran.kolibri.repository.financial.OperationRepository
import org.springframework.beans.factory.annotation.Autowired
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
    fun getAllOperationsByProjectId(projectId: Long): List<Operation> {
        return projectService.getFinancialProjectById(projectId).operations
    }

    @Transactional
    fun addIncomeOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String) {
        val account = accountService.changeAccountMoney(accountId, moneyAmount)
        val operation = IncomeOperation()
        operation.incomeAccount = account
        operation.resultMoneyAmount = account.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment)
    }

    @Transactional
    fun addExpendOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String) {
        val account = accountService.changeAccountMoney(accountId, -moneyAmount)
        val operation = ExpendOperation()
        operation.expendAccount = account
        operation.resultMoneyAmount = account.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment)
    }

    @Transactional
    fun addTransferOperation(projectId: Long, fromAccountId: Long, toAccountId: Long,
                             operationCategoryId: Long, moneyAmount: Double, comment: String) {
        val fromAccount = accountService.changeAccountMoney(fromAccountId, -moneyAmount)
        val toAccount = accountService.changeAccountMoney(toAccountId, moneyAmount)
        val operation = TransferOperation()
        operation.fromAccount = fromAccount
        operation.toAccount = toAccount
        operation.fromAccountResultMoneyAmount = fromAccount.currentMoneyAmount
        operation.toAccountResultMoneyAmount = toAccount.currentMoneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment)
    }

    private fun addOperation(operation: Operation, projectId: Long, operationCategoryId: Long,
                             moneyAmount: Double, comment: String) {
        val project = projectService.getFinancialProjectById(projectId)
        val operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)
        operation.operationCategory = operationCategory
        operation.moneyAmount = moneyAmount
        operation.comment = comment
        operation.operationDate = Date()
        operation.project = project
        operationRepository.save(operation)
    }

}