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

        private val OPERATION_CATEGORY_NAME = "Default Operation Category"
        private val FIRST_ACCOUNT_NAME = "Default First Account"
        private val SECOND_ACCOUNT_NAME = "Default Second Account"
        private val DEFAULT_COMMENT = "Default comment"

        private val INCOME_MONEY_AMOUNT = 1000.0
        private val EXPEND_MONEY_AMOUNT = 200.0
        private val TRANSFER_MONEY_AMOUNT = 300.0
        private val OPERATION_GROUPS_QUANTITY = 283
    }

    @Autowired
    lateinit var globalPropertyRepository: GlobalPropertyRepository
    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository
    @Autowired
    lateinit var operationService: OperationService

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
        val accounts = fillAccounts(projects[0])
        val operationCategories = fillOperationCategories(projects[0])
        fillOperations(projects[0], accounts[0], accounts[1], operationCategories[0])

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
        val firstAccount = fillAccount(project, FIRST_ACCOUNT_NAME)
        val secondAccount = fillAccount(project, SECOND_ACCOUNT_NAME)
        return listOf(firstAccount, secondAccount)
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
        val operationCategory = OperationCategory()
        operationCategory.name = OPERATION_CATEGORY_NAME
        operationCategory.project = project
        operationCategoryRepository.save(operationCategory)
        return listOf(operationCategory)
    }

    private fun fillOperations(project: FinancialProject, firstAccount: Account,
                               secondAccount: Account, operationCategory: OperationCategory) {
        for (i in 1..OPERATION_GROUPS_QUANTITY) {
            operationService.addIncomeOperation(project.id!!, firstAccount.id!!, operationCategory.id!!,
                    INCOME_MONEY_AMOUNT, "")
            operationService.addIncomeOperation(project.id!!, secondAccount.id!!, operationCategory.id!!,
                    INCOME_MONEY_AMOUNT, DEFAULT_COMMENT)
            operationService.addExpendOperation(project.id!!, firstAccount.id!!, operationCategory.id!!,
                    EXPEND_MONEY_AMOUNT, DEFAULT_COMMENT)
            operationService.addTransferOperation(project.id!!, secondAccount.id!!, firstAccount.id!!,
                    operationCategory.id!!, TRANSFER_MONEY_AMOUNT, DEFAULT_COMMENT)
        }
    }

}
