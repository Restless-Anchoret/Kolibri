package com.ran.kolibri.specification.financial

import com.ran.kolibri.entity.financial.OperationCategory
import com.ran.kolibri.entity.project.FinancialProject
import org.springframework.data.jpa.domain.Specification

object OperationCategorySpecificationFactory {

    fun byProjectId(projectId: Long?): Specification<OperationCategory>? {
        return if (projectId == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<FinancialProject>("project").get<Long>("id"), projectId)
        }
    }

}
