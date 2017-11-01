package com.ran.kolibri.dto.response.financial

data class FinancialProjectSettingsDto(
        val defaultAccount: AccountDto? = null,
        val defaultOperationCategory: OperationCategoryDto? = null
)
