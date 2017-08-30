package com.ran.kolibri.service

import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.AccountRepository
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.repository.financial.OperationRepository
import com.ran.kolibri.repository.project.FinancialProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FinancialProjectService {

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

    @Transactional
    fun getFinancialProjectById(projectId: Long): FinancialProject {
        return financialProjectRepository.findOne(projectId)
                ?: throw NotFoundException("Financial Project was not found")
    }

    @Transactional
    fun deleteFinancialProject(projectId: Long) {
        val operations = operationService.getOperationsByProjectId(projectId)
        operationRepository.delete(operations)

        val accounts = accountService.getAccountsByProjectId(projectId)
        accountRepository.delete(accounts)

        val operationCategories = operationCategoryService.getOperationCategoriesByProjectId(projectId)
        operationCategoryRepository.delete(operationCategories)

        financialProjectRepository.delete(projectId)
    }

    @Transactional
    fun createFinancialProjectFromTemplate(projectId: Long, name: String,
                                           description: String, isTemplate: Boolean): FinancialProject {
        return FinancialProject()
    }

}
