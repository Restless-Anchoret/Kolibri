package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Account
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

interface AccountRepository : CrudRepository<Account, Long> {

    @Query("select acc " +
            "from Account as acc " +
            "where acc.project.id = :projectId")
    fun findByProjectId(@Param("projectId") projectId: Long, pageable: Pageable? = null): Page<Account>

}
