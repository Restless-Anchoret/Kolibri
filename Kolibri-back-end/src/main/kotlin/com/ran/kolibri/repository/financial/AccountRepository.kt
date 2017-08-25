package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AccountRepository : JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {

    @Query("select acc " +
            "from Account as acc " +
            "where acc.project.id = :projectId")
    fun findByProjectId(@Param("projectId") projectId: Long, pageable: Pageable? = null,
                        specification: Specification<Account>): Page<Account>

}
