package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.OperationCategoryRepository
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.financial.OperationCategorySpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OperationCategoryService {

    @Autowired
    lateinit var financialProjectService: FinancialProjectService

    @Autowired
    lateinit var operationCategoryRepository: OperationCategoryRepository

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

}
