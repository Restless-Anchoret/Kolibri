package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.OperationCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface OperationCategoryRepository : JpaRepository<OperationCategory, Long>,
        JpaSpecificationExecutor<OperationCategory>
