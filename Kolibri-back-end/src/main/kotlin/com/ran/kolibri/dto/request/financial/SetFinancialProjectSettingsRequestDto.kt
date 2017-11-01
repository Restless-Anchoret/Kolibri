package com.ran.kolibri.dto.request.financial

data class SetFinancialProjectSettingsRequestDto(
        var accountId: Long? = null,
        var operationCategoryId: Long? = null
)
