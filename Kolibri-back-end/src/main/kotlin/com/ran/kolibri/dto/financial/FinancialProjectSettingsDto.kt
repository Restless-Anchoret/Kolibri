package com.ran.kolibri.dto.financial

data class FinancialProjectSettingsDto(
        val defaultAccount: AccountDto? = null,
        val defaultOperationCategory: OperationCategoryDto? = null
)
