package com.ran.kolibri.dto.financial

class ExpendOperationDto : OperationDto(operationType = EXPEND_OPERATION_TYPE) {

    var expendAccount: AccountDto? = null
    var resultMoneyAmount: Double? = null

}