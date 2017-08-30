package com.ran.kolibri.service

import com.ran.kolibri.app.ApplicationProfile.DEV
import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.entity.property.GlobalProperty
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.repository.property.GlobalPropertyRepository
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Profile(DEV)
class DebugDataService {

    companion object {
        private val LOGGER = Logger.getLogger(DebugDataService::class.java)

        private val DEBUG_DATA_PROPERTY_KEY = "debug.data"
        private val DEBUG_DATA_PROPERTY_VALUE = "populated"

        private val TEMPLATE_PROJECT_NAME = "Template Project"
        private val FINANCIAL_PROJECT_NAME = "Financial Project"

        private val ACCOUNTS_QUANTITY = 8
        private val OPERATION_CATEGORIES_QUANTITY = 20
    }

    @Autowired
    lateinit var globalPropertyRepository: GlobalPropertyRepository
    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository

    @Transactional
    fun populateDebugData() {
        LOGGER.logInfo{ "Before debug data population" }

        val debugDataProperty = globalPropertyRepository.findByKey(DEBUG_DATA_PROPERTY_KEY)
        if (debugDataProperty != null) {
            LOGGER.logInfo{ "Debug data has been already populated" }
            return
        }

        fillDebugDataProperty()
        val projects = fillFinancialProjects()
        fillAccounts(projects[0])
        fillOperationCategories(projects[0])

        LOGGER.logInfo{ "After debug data population" }
    }

    private fun fillDebugDataProperty() {
        val createdDebugDataProperty = GlobalProperty()
        createdDebugDataProperty.key = DEBUG_DATA_PROPERTY_KEY
        createdDebugDataProperty.value = DEBUG_DATA_PROPERTY_VALUE
        globalPropertyRepository.save(createdDebugDataProperty)
    }

    private fun fillFinancialProjects(): List<FinancialProject> {
        val templateProject = FinancialProject()
        templateProject.name = TEMPLATE_PROJECT_NAME
        templateProject.isTemplate = true
        projectRepository.save(templateProject)

        val financialProject = FinancialProject()
        financialProject.name = FINANCIAL_PROJECT_NAME
        financialProject.isTemplate = false
        projectRepository.save(financialProject)
        return listOf(financialProject, templateProject)
    }

    private fun fillAccounts(project: FinancialProject): List<Account> {
        return (1..ACCOUNTS_QUANTITY).map { index ->
            fillAccount(project, "Account #$index")
        }
    }

    private fun fillAccount(project: FinancialProject, accountName: String): Account {
        val account = Account()
        account.name = accountName
        account.creationDate = Date()
        account.project = project
        accountRepository.save(account)
        return account
    }

    private fun fillOperationCategories(project: FinancialProject): List<OperationCategory> {
        return (1..OPERATION_CATEGORIES_QUANTITY).map { index ->
            val indexPrefix = if (index < 10) "0" else ""
            val name = "Operation Category #$indexPrefix$index"
            fillOperationCategory(project, name)
        }
    }

    private fun fillOperationCategory(project: FinancialProject, operationCategoryName: String): OperationCategory {
        val operationCategory = OperationCategory()
        operationCategory.name = operationCategoryName
        operationCategory.project = project
        operationCategoryRepository.save(operationCategory)
        return operationCategory
    }

}
