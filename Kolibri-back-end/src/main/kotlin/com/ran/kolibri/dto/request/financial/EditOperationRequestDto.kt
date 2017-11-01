package com.ran.kolibri.dto.request.financial

data class EditOperationRequestDto(
        var operationCategoryId: Long? = null,
        var moneyAmount: Double? = null
)
