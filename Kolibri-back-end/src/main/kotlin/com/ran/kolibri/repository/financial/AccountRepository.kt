package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Account
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AccountRepository : JpaRepository<Account, Long>, JpaSpecificationExecutor<Account>
