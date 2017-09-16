package com.ran.kolibri.service

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
        return FinancialProject()
    }

}
