package com.ran.kolibri.specification.financial

import com.ran.kolibri.entity.financial.Operation
import com.ran.kolibri.entity.project.FinancialProject
import org.springframework.data.jpa.domain.Specification

object OperationSpecificationFactory {

    fun <T: Operation> byProjectId(projectId: Long?): Specification<T>? {
        return if (projectId == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<FinancialProject>("project").get<Long>("id"), projectId)
        }
    }

}
