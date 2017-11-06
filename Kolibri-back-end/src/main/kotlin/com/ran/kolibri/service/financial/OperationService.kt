package com.ran.kolibri.service.financial

import com.ran.kolibri.component.DateUtils
import com.ran.kolibri.entity.financial.ExpendOperation
import com.ran.kolibri.entity.financial.IncomeOperation
import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.financial.TransferOperation
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.extension.logDebug
import com.ran.kolibri.repository.financial.ExpendOperationRepository
import com.ran.kolibri.repository.financial.IncomeOperationRepository
import com.ran.kolibri.repository.financial.OperationRepository
import com.ran.kolibri.repository.financial.TransferOperationRepository
import com.ran.kolibri.service.comment.CommentService
import com.ran.kolibri.specification.financial.OperationSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OperationService {

    @Autowired
    lateinit var financialProjectService: FinancialProjectService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationCategoryService: OperationCategoryService
    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var operationRepository: OperationRepository
    @Autowired
    lateinit var incomeOperationRepository: IncomeOperationRepository
    @Autowired
    lateinit var expendOperationRepository: ExpendOperationRepository
    @Autowired
    lateinit var transferOperationRepository: TransferOperationRepository

    @Autowired
    lateinit var dateUtils: DateUtils

    @Transactional
    fun getOperationsByProjectId(projectId: Long, pageable: Pageable? = null): Page<Operation> {
        financialProjectService.getFinancialProjectById(projectId)
        val specification = Specifications.where(OperationSpecificationFactory.byProjectId(projectId))
        return operationRepository.findAll(specification, pageable)
    }

    @Transactional
    fun getAllSortedOperationsByProjectId(projectId: Long): List<Operation> {
        financialProjectService.getFinancialProjectById(projectId)
        val specification = Specifications.where(OperationSpecificationFactory.byProjectId(projectId))
        return operationRepository.findAll(specification,
                Sort("operationDate", "id"))
    }

    @Transactional
    fun getOperationById(operationId: Long): Operation {
        return operationRepository.findOne(operationId) ?: throw NotFoundException("Operation was not found")
    }

    @Transactional
    fun addIncomeOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date) {
        logDebug { "Adding income operation: projectId: $projectId, accountId: $accountId, " +
                "operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(accountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val previousRemainder = accountService.findAccountMoneyAmountForDate(accountId, correctedDate)
        val resultMoneyAmount = previousRemainder + moneyAmount
        correctAccountAndOperationsByMoneyDelta(accountId, correctedDate, null, moneyAmount)

        val operation = IncomeOperation(moneyAmount, resultMoneyAmount, correctedDate)
        operation.incomeAccount = accountService.getAccountById(accountId)
        addOperation(operation, projectId, operationCategoryId, comment)
    }

    @Transactional
    fun addExpendOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date) {
        logDebug { "Adding expend operation: projectId: $projectId, accountId: $accountId, " +
                "operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(accountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val previousRemainder = accountService.findAccountMoneyAmountForDate(accountId, correctedDate)
        val resultMoneyAmount = previousRemainder - moneyAmount
        correctAccountAndOperationsByMoneyDelta(accountId, correctedDate, null, -moneyAmount)

        val operation = ExpendOperation(moneyAmount, resultMoneyAmount, correctedDate)
        operation.expendAccount = accountService.getAccountById(accountId)
        addOperation(operation, projectId, operationCategoryId, comment)
    }

    @Transactional
    fun addTransferOperation(projectId: Long, fromAccountId: Long, toAccountId: Long,
                             operationCategoryId: Long, moneyAmount: Double,
                             comment: String, operationDate: Date) {
        logDebug { "Adding transfer operation: projectId: $projectId, fromAccountId: $fromAccountId, " +
                "toAccountId: $toAccountId, operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(fromAccountId, projectId)
        ensureAccountBelongsToProject(toAccountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val fromAccountPreviousRemainder = accountService.findAccountMoneyAmountForDate(fromAccountId, correctedDate)
        val toAccountPreviousRemainder = accountService.findAccountMoneyAmountForDate(toAccountId, correctedDate)
        val fromAccountResultMoneyAmount = fromAccountPreviousRemainder - moneyAmount
        val toAccountResultMoneyAmount = toAccountPreviousRemainder + moneyAmount
        correctAccountAndOperationsByMoneyDelta(fromAccountId, operationDate, null, -moneyAmount)
        correctAccountAndOperationsByMoneyDelta(toAccountId, operationDate, null, moneyAmount)

        val operation = TransferOperation(moneyAmount, fromAccountResultMoneyAmount,
                toAccountResultMoneyAmount, correctedDate)
        operation.fromAccount = accountService.getAccountById(fromAccountId)
        operation.toAccount = accountService.getAccountById(toAccountId)
        addOperation(operation, projectId, operationCategoryId, comment)
    }

    private fun addOperation(operation: Operation, projectId: Long, operationCategoryId: Long,
                             comment: String) {
        val project = financialProjectService.getFinancialProjectById(projectId)
        val operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)
        operation.operationCategory = operationCategory
        operation.project = project
        operationRepository.save(operation)
        commentService.addComment(operation, operationRepository, comment)
    }

    @Transactional
    fun removeOperation(operationId: Long) {
        val operation = getOperationById(operationId)
        when (operation.javaClass) {
            IncomeOperation::class.java -> {
                operation as IncomeOperation
                correctAccountAndOperationsByMoneyDelta(operation.incomeAccount?.id!!,
                        operation.operationDate, operationId, -operation.moneyAmount)
            }
            ExpendOperation::class.java -> {
                operation as ExpendOperation
                correctAccountAndOperationsByMoneyDelta(operation.expendAccount?.id!!,
                        operation.operationDate, operationId, operation.moneyAmount)
            }
            TransferOperation::class.java -> {
                operation as TransferOperation
                correctAccountAndOperationsByMoneyDelta(operation.fromAccount?.id!!,
                        operation.operationDate, operationId, operation.moneyAmount)
                correctAccountAndOperationsByMoneyDelta(operation.toAccount?.id!!,
                        operation.operationDate, operationId, -operation.moneyAmount)
            }
        }
        operationRepository.delete(operation)
    }

    @Transactional
    fun editOperation(operationId: Long, operationCategoryId: Long, moneyAmount: Double) {
        val operation = getOperationById(operationId)

        operation.operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)

        if (Math.abs(moneyAmount - operation.moneyAmount) < 1e-8) {
            operationRepository.save(operation)
            return
        }

        when (operation.javaClass) {
            IncomeOperation::class.java -> {
                operation as IncomeOperation
                correctAccountAndOperationsByMoneyDelta(operation.incomeAccount?.id!!,
                        operation.operationDate, operationId, moneyAmount - operation.moneyAmount)
            }
            ExpendOperation::class.java -> {
                operation as ExpendOperation
                correctAccountAndOperationsByMoneyDelta(operation.expendAccount?.id!!,
                        operation.operationDate, operationId, operation.moneyAmount - moneyAmount)
            }
            TransferOperation::class.java -> {
                operation as TransferOperation
                correctAccountAndOperationsByMoneyDelta(operation.fromAccount?.id!!,
                        operation.operationDate, operationId, operation.moneyAmount - moneyAmount)
                correctAccountAndOperationsByMoneyDelta(operation.toAccount?.id!!,
                        operation.operationDate, operationId, moneyAmount - operation.moneyAmount)
            }
        }

        operation.moneyAmount = moneyAmount
        operationRepository.save(operation)
    }

    private fun correctAccountAndOperationsByMoneyDelta(accountId: Long, operationDate: Date,
                                                        operationId: Long?, moneyDelta: Double) {
        accountService.changeAccountMoney(accountId, moneyDelta)

        val incomeSpecification = Specifications.where(OperationSpecificationFactory.byIncomeAccount(accountId))
                .and(OperationSpecificationFactory.afterDateAndId(operationDate, operationId))
        val incomeOperations = incomeOperationRepository.findAll(incomeSpecification)
        incomeOperations.forEach {
            it.resultMoneyAmount += moneyDelta
        }
        incomeOperationRepository.save(incomeOperations)

        val expendSpecification = Specifications.where(OperationSpecificationFactory.byExpendAccount(accountId))
                .and(OperationSpecificationFactory.afterDateAndId(operationDate, operationId))
        val expendOperations = expendOperationRepository.findAll(expendSpecification)
        expendOperations.forEach {
            it.resultMoneyAmount += moneyDelta
        }
        expendOperationRepository.save(expendOperations)

        val transferFromSpecification = Specifications.where(OperationSpecificationFactory.byTransferFromAccount(accountId))
                .and(OperationSpecificationFactory.afterDateAndId(operationDate, operationId))
        val transferFromOperations = transferOperationRepository.findAll(transferFromSpecification)
        transferFromOperations.forEach {
            it.fromAccountResultMoneyAmount += moneyDelta
        }
        transferOperationRepository.save(transferFromOperations)

        val transferToSpecification = Specifications.where(OperationSpecificationFactory.byTransferToAccount(accountId))
                .and(OperationSpecificationFactory.afterDateAndId(operationDate, operationId))
        val transferToOperations = transferOperationRepository.findAll(transferToSpecification)
        transferToOperations.forEach {
            it.toAccountResultMoneyAmount += moneyDelta
        }
        transferOperationRepository.save(transferToOperations)
    }

    private fun ensureOperationCategoryBelongsToProject(operationCategoryId: Long, projectId: Long) {
        val operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)
        if (operationCategory.project?.id != projectId) {
            throw BadRequestException("Operation Category does not belong to Project")
        }
    }

    private fun ensureAccountBelongsToProject(accountId: Long, projectId: Long) {
        val account = accountService.getAccountById(accountId)
        if (account.project?.id != projectId) {
            throw BadRequestException("Account does not belong to Project")
        }
    }

    @Transactional
    fun addOperationComment(operationId: Long, commentText: String) {
        val operation = getOperationById(operationId)
        commentService.addComment(operation, operationRepository, commentText)
    }

    @Transactional
    fun editOperationComment(operationId: Long, commentIndex: Int, commentText: String) {
        val operation = getOperationById(operationId)
        commentService.editComment(operation, commentIndex, commentText)
    }

    @Transactional
    fun removeOperationComment(operationId: Long, commentIndex: Int) {
        val operation = getOperationById(operationId)
        commentService.removeComment(operation, operationRepository, commentIndex)
    }

}
