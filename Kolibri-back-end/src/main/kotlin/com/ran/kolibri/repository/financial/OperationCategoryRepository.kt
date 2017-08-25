package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.OperationCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OperationCategoryRepository : JpaRepository<OperationCategory, Long>,
        JpaSpecificationExecutor<OperationCategory> {

    @Query("select cat " +
            "from OperationCategory as cat " +
            "where cat.project.id = :projectId")
    fun findByProjectId(@Param("projectId") projectId: Long, pageable: Pageable? = null,
                        specification: Specification<OperationCategory>): Page<OperationCategory>

}
