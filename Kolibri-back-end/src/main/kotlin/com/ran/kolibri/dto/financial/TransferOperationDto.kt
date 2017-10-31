package com.ran.kolibri.dto.financial

class TransferOperationDto : OperationDto(operationType = TRANSFER_OPERATION_TYPE) {

    var fromAccount: AccountDto? = null
    var fromAccountResultMoneyAmount: Double? = null
    var toAccount: AccountDto? = null
    var toAccountResultMoneyAmount: Double? = null

}