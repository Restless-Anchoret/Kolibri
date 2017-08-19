package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.OperationCategory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param

interface OperationCategoryRepository : PagingAndSortingRepository<OperationCategory, Long> {

    @Query("select cat " +
            "from OperationCategory as cat " +
            "where cat.project.id = :projectId")
    fun findByProjectId(@Param("projectId") projectId: Long, pageable: Pageable? = null): Page<OperationCategory>

}
