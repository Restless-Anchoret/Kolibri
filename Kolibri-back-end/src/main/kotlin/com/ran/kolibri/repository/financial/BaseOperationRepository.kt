package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.query.Param

@NoRepositoryBean
interface BaseOperationRepository<T: Operation> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    @Query("select op " +
            "from #{#entityName} as op " +
            "where op.project.id = :projectId " +
            "order by op.operationDate desc, op.id desc")
    fun findByProject(@Param("projectId") projectId: Long, pageable: Pageable? = null): Page<T>

}
