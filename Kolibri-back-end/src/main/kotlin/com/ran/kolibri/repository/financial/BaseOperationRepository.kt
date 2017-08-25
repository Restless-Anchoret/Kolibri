package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Operation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseOperationRepository<T: Operation> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
