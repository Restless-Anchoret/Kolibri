package com.ran.kolibri.component.startup

import com.ran.kolibri.app.ApplicationProfile.DEV
import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.entity.user.User
import com.ran.kolibri.entity.user.UserRole.ORDINARY_USER
import com.ran.kolibri.entity.user.UserStatus.DISABLED
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.extension.map
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.repository.user.UserRepository
import com.ran.kolibri.repository.vcs.GitRepositoryRepository
import com.ran.kolibri.security.JwtAuthentication
import com.ran.kolibri.security.UserData
import com.ran.kolibri.service.financial.OperationService
import com.ran.kolibri.service.project.ProjectService
import com.ran.kolibri.service.user.UserService
import com.ran.kolibri.service.vcs.GitRepositoryManagementService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Component
@Profile(DEV)
class DebugDataPopulator {

    companion object {
        private val DISABLED_USER_LOGIN = "disabled.user"

        private val TEMPLATE_PROJECT_NAME = "Default Template Project"
        private val FINANCIAL_PROJECT_NAME = "Default Financial Project"
        private val DISABLED_USER_PROJECT_NAME = "Disabled User Financial Project"

        private val ACCOUNTS_QUANTITY = 8
        private val OPERATION_CATEGORIES_QUANTITY = 20

        private val DEFAULT_DESCRIPTION = "Default description"

        private val TEST_REPOSITORY_NAME = "Kolibri Test Repository"
        private val TEST_REPOSITORY_URL = "https://bitbucket.org/RestlessAnchoret/kolibri-repository-test"
        private val TEST_REPOSITORY_USERNAME = "RestlessAnchoret"
        private val TEST_REPOSITORY_PASSWORD = "bitbeternal23"
    }

    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository
    @Autowired
    lateinit var userRepository: UserRepository
    @Autowired
    lateinit var gitRepositoryRepository: GitRepositoryRepository

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var operationService: OperationService
    @Autowired
    lateinit var gitRepositoryManagementService: GitRepositoryManagementService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade

    @EventListener
    @Transactional
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        logInfo { "Before debug data population" }
        fillEntities()
        logInfo { "After debug data population" }
    }

    private fun fillEntities() {
        val adminUser = userService.getAdminUser()
        val adminUserData = orikaMapperFacade.map<UserData>(adminUser)
        SecurityContextHolder.getContext().authentication = JwtAuthentication(adminUserData)

        val defaultFinancialProject = projectService.getProjects(false, FINANCIAL_PROJECT_NAME, null)
        if (defaultFinancialProject.hasContent()) {
            logInfo { "Debug data has been already populated" }
            return
        }

        val users = fillUsers()
        val projects = fillFinancialProjects(users[0])
        val accounts = fillAccounts(projects[0])
        val operationCategories = fillOperationCategories(projects[0])
        fillOperations(projects[0], accounts, operationCategories)
        fillGitRepository(projects[0])
    }

    private fun fillUsers(): List<User> {
        val disabledUser = User(DISABLED_USER_LOGIN, "fake", ORDINARY_USER, DISABLED)
        userRepository.save(disabledUser)
        return listOf(disabledUser)
    }

    private fun fillFinancialProjects(disabledUser: User): List<FinancialProject> {
        val templateProject = projectService.createEmptyFinancialProject(
                TEMPLATE_PROJECT_NAME, DEFAULT_DESCRIPTION, true)
        val financialProject = projectService.createEmptyFinancialProject(
                FINANCIAL_PROJECT_NAME, DEFAULT_DESCRIPTION, false)
        val disabledUserProject = projectService.createEmptyFinancialProject(
                DISABLED_USER_PROJECT_NAME, DEFAULT_DESCRIPTION, false, disabledUser)
        return listOf(financialProject, templateProject, disabledUserProject)
    }

    private fun fillAccounts(project: FinancialProject): List<Account> {
        return (1..ACCOUNTS_QUANTITY).map { index ->
            fillAccount(project, "Account #$index")
        }
    }

    private fun fillAccount(project: FinancialProject, accountName: String): Account {
        val account = Account(accountName, DEFAULT_DESCRIPTION)
        account.project = project
        account.currentMoneyAmount = 1000.0
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

    private fun fillOperations(project: FinancialProject,
                               accounts: List<Account>,
                               operationCategories: List<OperationCategory>) {
        val now = Date()
        operationService.addIncomeOperation(project.id!!, accounts[2].id!!,
                operationCategories[4].id!!, 500.0, DEFAULT_DESCRIPTION, now)
        operationService.addExpendOperation(project.id!!, accounts[3].id!!,
                operationCategories[8].id!!, 300.0, DEFAULT_DESCRIPTION, now)
        operationService.addTransferOperation(project.id!!, accounts[7].id!!, accounts[0].id!!,
                operationCategories[13].id!!, 200.0, DEFAULT_DESCRIPTION, now)
    }

    private fun fillGitRepository(project: Project): GitRepository {
        val gitRepository = gitRepositoryManagementService.createGitRepository(TEST_REPOSITORY_NAME, DEFAULT_DESCRIPTION,
                TEST_REPOSITORY_URL, TEST_REPOSITORY_USERNAME, TEST_REPOSITORY_PASSWORD)
        gitRepository.projects.add(project)
        gitRepositoryRepository.save(gitRepository)
        return gitRepository
    }

}
