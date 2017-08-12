package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.OperationCategory
import org.springframework.data.repository.CrudRepository

interface OperationCategoryRepository : CrudRepository<OperationCategory, Long>