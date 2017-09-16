package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.ExpendOperation
import com.ran.kolibri.entity.financial.FinancialProjectSettings
import com.ran.kolibri.entity.financial.IncomeOperation
import com.ran.kolibri.entity.financial.TransferOperation
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.extension.logDebug
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.FinancialProjectSettingsRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.repository.financial.OperationRepository
import com.ran.kolibri.repository.project.FinancialProjectRepository
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FinancialProjectService {

    companion object {
        private val LOGGER = Logger.getLogger(FinancialProjectService::class.java)
    }

    @Autowired
    lateinit var operationService: OperationService
    @Autowired
    lateinit var accountService: AccountService
    @Autowired
    lateinit var operationCategoryService: OperationCategoryService
    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var financialProjectRepository: FinancialProjectRepository
    @Autowired
    lateinit var operationRepository: OperationRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository
    @Autowired
    lateinit var financialProjectSettingsRepository: FinancialProjectSettingsRepository

    @Transactional
    fun getFinancialProjectById(projectId: Long): FinancialProject {
        return financialProjectRepository.findOne(projectId)
                ?: throw NotFoundException("Financial Project was not found")
    }

    @Transactional
    fun setFinancialProjectSettings(projectId: Long, accountId: Long?, operationCategoryId: Long?): FinancialProject {
        LOGGER.logInfo { "Setting Financial Project settings: projectId = $projectId, " +
                "accountId = $accountId, operationCategoryId = $operationCategoryId" }
        val project = getFinancialProjectById(projectId)

        project.settings!!.defaultAccount = if (accountId == null) null else
            accountService.getAccountById(accountId)
        LOGGER.logDebug { "Default Account: ${project.settings!!.defaultAccount}" }

        project.settings!!.defaultOperationCategory = if (operationCategoryId == null) null else
            operationCategoryService.getOperationCategoryById(operationCategoryId)
        LOGGER.logDebug { "Default Operation Category: ${project.settings!!.defaultOperationCategory}" }

        financialProjectRepository.save(project)
        LOGGER.logInfo { "Financial Project settings were saved" }
        return project
    }

    @Transactional
    fun deleteFinancialProject(projectId: Long) {
        val project = getFinancialProjectById(projectId)
        financialProjectSettingsRepository.delete(project.settings)

        val operations = operationService.getOperationsByProjectId(projectId)
        operationRepository.delete(operations)

        val accounts = accountService.getAccountsByProjectId(projectId)
        accountRepository.delete(accounts)

        val operationCategories = operationCategoryService.getOperationCategoriesByProjectId(projectId)
        operationCategoryRepository.delete(operationCategories)

        financialProjectRepository.delete(project)
    }

    @Transactional
    fun createFinancialProjectFromTemplate(projectId: Long, name: String,
                                           description: String, isTemplate: Boolean): FinancialProject {
        val templateProject = getFinancialProjectById(projectId)

        val newProject = FinancialProject()
        newProject.name = name
        newProject.description = description
        newProject.isTemplate = isTemplate
        financialProjectRepository.save(newProject)
        commentService.cloneComments(templateProject, newProject, financialProjectRepository)

        val accountIdsMap = HashMap<Long, Long>()
        templateProject.accounts.forEach {
            val clonedAccount = it.clone()
            clonedAccount.project = newProject
            accountRepository.save(clonedAccount)
            commentService.cloneComments(it, clonedAccount, accountRepository)
            accountIdsMap[it.id!!] = clonedAccount.id!!
        }

        val operationCategoryIdsMap = HashMap<Long, Long>()
        templateProject.operationCategories.forEach {
            val clonedOperationCategory = it.clone()
            clonedOperationCategory.project = newProject
            operationCategoryRepository.save(clonedOperationCategory)
            commentService.cloneComments(it, clonedOperationCategory, operationCategoryRepository)
            operationCategoryIdsMap[it.id!!] = clonedOperationCategory.id!!
        }

        val operations = operationService.getAllSortedOperationsByProjectId(projectId)
        operations.forEach { operation ->
            val clonedOperation = operation.clone()
            clonedOperation.project = newProject
            clonedOperation.operationCategory = operationCategoryRepository
                    .findOne(operationCategoryIdsMap[operation.operationCategory?.id!!])
            when (operation.javaClass) {
                IncomeOperation::class.java -> {
                    operation as IncomeOperation
                    clonedOperation as IncomeOperation
                    clonedOperation.incomeAccount = accountRepository
                            .findOne(accountIdsMap[operation.incomeAccount?.id!!])
                }
                ExpendOperation::class.java -> {
                    operation as ExpendOperation
                    clonedOperation as ExpendOperation
                    clonedOperation.expendAccount = accountRepository
                            .findOne(accountIdsMap[operation.expendAccount?.id!!])
                }
                TransferOperation::class.java -> {
                    operation as TransferOperation
                    clonedOperation as TransferOperation
                    clonedOperation.fromAccount = accountRepository
                            .findOne(accountIdsMap[operation.fromAccount?.id!!])
                    clonedOperation.toAccount = accountRepository
                            .findOne(accountIdsMap[operation.toAccount?.id!!])
                }
            }
            commentService.cloneComments(operation, clonedOperation, operationRepository)
        }

        val settings = FinancialProjectSettings()
        settings.financialProject = newProject
        if (templateProject.settings?.defaultAccount != null) {
            settings.defaultAccount = accountRepository.findOne(
                    accountIdsMap[templateProject.settings?.defaultAccount?.id!!])
        }
        if (templateProject.settings?.defaultOperationCategory != null) {
            settings.defaultOperationCategory = operationCategoryRepository.findOne(
                    accountIdsMap[templateProject.settings?.defaultOperationCategory?.id!!])
        }
        financialProjectSettingsRepository.save(settings)
        newProject.settings = settings

        return newProject
    }

}
