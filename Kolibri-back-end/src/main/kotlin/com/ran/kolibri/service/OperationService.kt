package com.ran.kolibri.service

import com.ran.kolibri.component.DateUtils
import com.ran.kolibri.entity.financial.*
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.extension.logDebug
import com.ran.kolibri.repository.financial.*
import com.ran.kolibri.specification.financial.OperationSpecificationFactory
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class OperationService {

    companion object {
        private val LOGGER = Logger.getLogger(OperationService::class.java)
    }

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
    fun getOperationById(operationId: Long): Operation {
        return operationRepository.findOne(operationId) ?: throw NotFoundException("Operation was not found")
    }

    @Transactional
    fun addIncomeOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date) {
        LOGGER.logDebug { "Adding income operation: projectId: $projectId, accountId: $accountId, " +
                "operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(accountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val previousRemainder = accountService.findAccountMoneyAmountForDate(accountId, correctedDate)
        correctAccountAndOperationsByMoneyDelta(accountId, correctedDate, null, moneyAmount)

        val operation = IncomeOperation()
        operation.incomeAccount = accountService.getAccountById(accountId)
        operation.resultMoneyAmount = previousRemainder + moneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, correctedDate)
    }

    @Transactional
    fun addExpendOperation(projectId: Long, accountId: Long, operationCategoryId: Long,
                           moneyAmount: Double, comment: String, operationDate: Date) {
        LOGGER.logDebug { "Adding expend operation: projectId: $projectId, accountId: $accountId, " +
                "operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(accountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val previousRemainder = accountService.findAccountMoneyAmountForDate(accountId, correctedDate)
        correctAccountAndOperationsByMoneyDelta(accountId, correctedDate, null, -moneyAmount)

        val operation = ExpendOperation()
        operation.expendAccount = accountService.getAccountById(accountId)
        operation.resultMoneyAmount = previousRemainder - moneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, correctedDate)
    }

    @Transactional
    fun addTransferOperation(projectId: Long, fromAccountId: Long, toAccountId: Long,
                             operationCategoryId: Long, moneyAmount: Double,
                             comment: String, operationDate: Date) {
        LOGGER.logDebug { "Adding transfer operation: projectId: $projectId, fromAccountId: $fromAccountId, " +
                "toAccountId: $toAccountId, operationCategoryId: $operationCategoryId, moneyAmount: $moneyAmount, " +
                "comment: $comment, operationDate: $operationDate - ${operationDate.time}" }
        ensureAccountBelongsToProject(fromAccountId, projectId)
        ensureAccountBelongsToProject(toAccountId, projectId)
        ensureOperationCategoryBelongsToProject(operationCategoryId, projectId)

        val correctedDate = dateUtils.getDateWithoutTime(operationDate)
        val fromAccountPreviousRemainder = accountService.findAccountMoneyAmountForDate(fromAccountId, correctedDate)
        val toAccountPreviousRemainder = accountService.findAccountMoneyAmountForDate(toAccountId, correctedDate)
        correctAccountAndOperationsByMoneyDelta(fromAccountId, operationDate, null, -moneyAmount)
        correctAccountAndOperationsByMoneyDelta(toAccountId, operationDate, null, moneyAmount)

        val operation = TransferOperation()
        operation.fromAccount = accountService.getAccountById(fromAccountId)
        operation.toAccount = accountService.getAccountById(toAccountId)
        operation.fromAccountResultMoneyAmount = fromAccountPreviousRemainder - moneyAmount
        operation.toAccountResultMoneyAmount = toAccountPreviousRemainder + moneyAmount
        addOperation(operation, projectId, operationCategoryId, moneyAmount, comment, operationDate)
    }

    private fun addOperation(operation: Operation, projectId: Long, operationCategoryId: Long,
                             moneyAmount: Double, comment: String, operationDate: Date) {
        val project = financialProjectService.getFinancialProjectById(projectId)
        val operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)
        operation.operationCategory = operationCategory
        operation.moneyAmount = moneyAmount
        operation.operationDate = dateUtils.getDateWithoutTime(operationDate)
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
                        operation.operationDate!!, operationId, -operation.moneyAmount)
            }
            ExpendOperation::class.java -> {
                operation as ExpendOperation
                correctAccountAndOperationsByMoneyDelta(operation.expendAccount?.id!!,
                        operation.operationDate!!, operationId, operation.moneyAmount)
            }
            TransferOperation::class.java -> {
                operation as TransferOperation
                correctAccountAndOperationsByMoneyDelta(operation.fromAccount?.id!!,
                        operation.operationDate!!, operationId, operation.moneyAmount)
                correctAccountAndOperationsByMoneyDelta(operation.toAccount?.id!!,
                        operation.operationDate!!, operationId, -operation.moneyAmount)
            }
        }
        operationRepository.delete(operation)
    }

    @Transactional
    fun editOperation(operationId: Long, operationCategoryId: Long, moneyAmount: Double) {
        val operation = operationRepository.findOne(operationId)

        operation.operationCategory = operationCategoryService.getOperationCategoryById(operationCategoryId)

        if (Math.abs(moneyAmount - operation.moneyAmount) < 1e-8) {
            operationRepository.save(operation)
            return
        }

        when (operation.javaClass) {
            IncomeOperation::class.java -> {
                operation as IncomeOperation
                correctAccountAndOperationsByMoneyDelta(operation.incomeAccount?.id!!,
                        operation.operationDate!!, operationId, moneyAmount - operation.moneyAmount)
            }
            ExpendOperation::class.java -> {
                operation as ExpendOperation
                correctAccountAndOperationsByMoneyDelta(operation.expendAccount?.id!!,
                        operation.operationDate!!, operationId, operation.moneyAmount - moneyAmount)
            }
            TransferOperation::class.java -> {
                operation as TransferOperation
                correctAccountAndOperationsByMoneyDelta(operation.fromAccount?.id!!,
                        operation.operationDate!!, operationId, operation.moneyAmount - moneyAmount)
                correctAccountAndOperationsByMoneyDelta(operation.toAccount?.id!!,
                        operation.operationDate!!, operationId, moneyAmount - operation.moneyAmount)
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

}
