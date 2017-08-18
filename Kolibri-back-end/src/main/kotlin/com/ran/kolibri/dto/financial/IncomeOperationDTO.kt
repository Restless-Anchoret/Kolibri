package com.ran.kolibri.dto.financial

class IncomeOperationDTO : OperationDTO(operationType = INCOME_OPERATION_TYPE) {

    var incomeAccount: AccountDTO? = null
    var resultMoneyAmount: Double? = null

}