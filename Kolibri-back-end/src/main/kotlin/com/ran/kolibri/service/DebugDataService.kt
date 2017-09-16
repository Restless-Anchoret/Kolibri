package com.ran.kolibri.service

import com.ran.kolibri.app.ApplicationProfile.DEV
import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
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

        private val TEMPLATE_PROJECT_NAME = "Default Template Project"
        private val FINANCIAL_PROJECT_NAME = "Default Financial Project"

        private val ACCOUNTS_QUANTITY = 8
        private val OPERATION_CATEGORIES_QUANTITY = 20
    }

    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository

    @Autowired
    lateinit var projectService: ProjectService

    @Transactional
    fun populateDebugData() {
        LOGGER.logInfo{ "Before debug data population" }

        val defaultFinancialProject = projectService.getProjects(false, FINANCIAL_PROJECT_NAME, null)
        if (defaultFinancialProject.hasContent()) {
            LOGGER.logInfo{ "Debug data has been already populated" }
            return
        }

        val projects = fillFinancialProjects()
        fillAccounts(projects[0])
        fillOperationCategories(projects[0])

        LOGGER.logInfo{ "After debug data population" }
    }

    private fun fillFinancialProjects(): List<FinancialProject> {
        val templateProject = projectService.createEmptyFinancialProject(
                TEMPLATE_PROJECT_NAME, "", true)
        val financialProject = projectService.createEmptyFinancialProject(
                FINANCIAL_PROJECT_NAME, "", false)
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
