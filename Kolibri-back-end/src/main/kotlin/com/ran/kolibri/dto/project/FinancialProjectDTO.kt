package com.ran.kolibri.dto.project

import com.ran.kolibri.dto.financial.AccountDTO
import com.ran.kolibri.dto.financial.OperationCategoryDTO

class FinancialProjectDTO : ProjectDTO(projectType = FINANCIAL_PROJECT_TYPE) {

    val defaultAccount: AccountDTO? = null
    val defaultOperationCategory: OperationCategoryDTO? = null

}
