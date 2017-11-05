package com.ran.kolibri.dto.export.financial

class IncomeOperationExportDto : OperationExportDto(operationType = INCOME_OPERATION_TYPE) {

    var incomeAccountId: Long? = null
    var resultMoneyAmount: Double? = null

}
