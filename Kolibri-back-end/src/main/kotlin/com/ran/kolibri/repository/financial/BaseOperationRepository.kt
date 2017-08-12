package com.ran.kolibri.repository.financial

import com.ran.kolibri.entity.financial.Operation
import org.springframework.data.repository.CrudRepository

interface BaseOperationRepository<T: Operation> : CrudRepository<T, Long>