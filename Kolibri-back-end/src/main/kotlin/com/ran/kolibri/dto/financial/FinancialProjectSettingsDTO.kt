package com.ran.kolibri.dto.financial

data class FinancialProjectSettingsDTO(
        val defaultAccount: AccountDTO? = null,
        val defaultOperationCategory: OperationCategoryDTO? = null
)
