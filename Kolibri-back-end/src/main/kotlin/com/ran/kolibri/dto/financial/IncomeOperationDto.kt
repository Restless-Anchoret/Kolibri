package com.ran.kolibri.dto.financial

class IncomeOperationDto : OperationDto(operationType = INCOME_OPERATION_TYPE) {

    var incomeAccount: AccountDto? = null
    var resultMoneyAmount: Double? = null

}