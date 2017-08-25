package com.ran.kolibri.specification.financial

import com.ran.kolibri.entity.financial.Account
import com.ran.kolibri.entity.project.FinancialProject
import org.springframework.data.jpa.domain.Specification

object AccountSpecificationFactory {

    fun byProjectId(projectId: Long?): Specification<Account>? {
        return if (projectId == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<FinancialProject>("project").get<Long>("id"), projectId)
        }
    }

}
