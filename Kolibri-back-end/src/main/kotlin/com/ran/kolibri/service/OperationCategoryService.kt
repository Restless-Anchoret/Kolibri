package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.repository.financial.OperationRepository
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.financial.OperationCategorySpecificationFactory
import com.ran.kolibri.specification.financial.OperationSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationCategoryService {

    @Autowired
    lateinit var financialProjectService: FinancialProjectService
    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository
    @Autowired
    lateinit var operationRepository: OperationRepository

    @Transactional
    fun getOperationCategoriesByProjectId(projectId: Long, name: String? = null,
                                          pageable: Pageable? = null): Page<OperationCategory> {
        financialProjectService.getFinancialProjectById(projectId)
        val specification = Specifications.where(OperationCategorySpecificationFactory.byProjectId(projectId))
                .and(BaseSpecificationFactory.byName<OperationCategory>(name))
        return operationCategoryRepository.findAll(specification, pageable)
    }

    @Transactional
    fun getOperationCategoryById(operationCategoryId: Long): OperationCategory {
        return operationCategoryRepository.findOne(operationCategoryId) ?:
                throw NotFoundException("Operation Category was not found")
    }

    @Transactional
    fun createOperationCategory(projectId: Long, name: String, description: String): OperationCategory {
        val project = financialProjectService.getFinancialProjectById(projectId)
        val operationCategory = OperationCategory()
        operationCategory.name = name
        operationCategory.description = description
        operationCategory.project = project
        operationCategoryRepository.save(operationCategory)
        return operationCategory
    }

    @Transactional
    fun editOperationCategory(operationCategoryId: Long, name: String, description: String): OperationCategory {
        val operationCategory = getOperationCategoryById(operationCategoryId)
        operationCategory.name = name
        operationCategory.description = description
        operationCategoryRepository.save(operationCategory)
        return operationCategory
    }

    @Transactional
    fun removeOperationCategory(operationCategoryId: Long) {
        val operationCategory = getOperationCategoryById(operationCategoryId)
        val operationsPage = operationRepository.findAll(
                OperationSpecificationFactory.byOperationCategoryId(operationCategoryId),
                PageRequest(0, 1))
        if (!operationsPage.content.isEmpty()) {
            throw BadRequestException("Operation Category cannot be removed, because it was used in Operations")
        }
        operationCategoryRepository.delete(operationCategory)
    }

    @Transactional
    fun addOperationCategoryComment(operationCategoryId: Long, commentText: String) {
        val operation = getOperationCategoryById(operationCategoryId)
        commentService.addComment(operation, operationCategoryRepository, commentText)
    }

    @Transactional
    fun editOperationCategoryComment(operationCategoryId: Long, commentIndex: Int, commentText: String) {
        val operation = getOperationCategoryById(operationCategoryId)
        commentService.editComment(operation, commentIndex, commentText)
    }

    @Transactional
    fun removeOperationCategoryComment(operationCategoryId: Long, commentIndex: Int) {
        val operation = getOperationCategoryById(operationCategoryId)
        commentService.removeComment(operation, operationCategoryRepository, commentIndex)
    }

}
