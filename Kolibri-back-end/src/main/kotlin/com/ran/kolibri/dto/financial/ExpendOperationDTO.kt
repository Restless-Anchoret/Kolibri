package com.ran.kolibri.dto.financial

class ExpendOperationDTO: OperationDTO(operationType = EXPEND_OPERATION_TYPE) {

    var expendAccount: AccountDTO? = null
    var resultMoneyAmount: Double? = null

}