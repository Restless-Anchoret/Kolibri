package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationCategoryService {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository

    @Transactional
    fun getAllOperationCategoriesByProjectId(projectId: Long): List<OperationCategory> {
        return projectService.getFinancialProjectById(projectId).operationCategories
    }

    @Transactional
    fun getOperationCategoryById(operationCategoryId: Long): OperationCategory {
        return operationCategoryRepository.findOne(operationCategoryId) ?:
                throw NotFoundException("Project was not found for id: $operationCategoryId")
    }

}