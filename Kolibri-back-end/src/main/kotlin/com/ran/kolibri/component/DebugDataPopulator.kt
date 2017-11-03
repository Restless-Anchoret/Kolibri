package com.ran.kolibri.component

import com.ran.kolibri.app.ApplicationProfile.DEV
import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.service.ProjectService
import com.ran.kolibri.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Profile(DEV)
class DebugDataPopulator {

    companion object {
        private val TEMPLATE_PROJECT_NAME = "Default Template Project"
        private val FINANCIAL_PROJECT_NAME = "Default Financial Project"

        private val ACCOUNTS_QUANTITY = 8
        private val OPERATION_CATEGORIES_QUANTITY = 20

        private val DEFAULT_DESCRIPTION = "Default description"
    }

    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var projectService: ProjectService

    @EventListener
    @Transactional
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        logInfo { "Before debug data population" }
        fillEntities()
        logInfo { "After debug data population" }
    }

    private fun fillEntities() {
        val defaultFinancialProject = projectService.getProjects(false, FINANCIAL_PROJECT_NAME, null)
        if (defaultFinancialProject.hasContent()) {
            logInfo { "Debug data has been already populated" }
            return
        }

        val projects = fillFinancialProjects()
        fillAccounts(projects[0])
        fillOperationCategories(projects[0])
    }

    private fun fillFinancialProjects(): List<FinancialProject> {
        val adminUser = userService.getAdminUser()
        val templateProject = projectService.createEmptyFinancialProject(
                TEMPLATE_PROJECT_NAME, DEFAULT_DESCRIPTION, true, adminUser)
        val financialProject = projectService.createEmptyFinancialProject(
                FINANCIAL_PROJECT_NAME, DEFAULT_DESCRIPTION, false, adminUser)
        return listOf(financialProject, templateProject)
    }

    private fun fillAccounts(project: FinancialProject): List<Account> {
        return (1..ACCOUNTS_QUANTITY).map { index ->
            fillAccount(project, "Account #$index")
        }
    }

    private fun fillAccount(project: FinancialProject, accountName: String): Account {
        val account = Account(accountName, DEFAULT_DESCRIPTION)
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
        val operationCategory = OperationCategory(operationCategoryName, DEFAULT_DESCRIPTION)
        operationCategory.project = project
        operationCategoryRepository.save(operationCategory)
        return operationCategory
    }

}