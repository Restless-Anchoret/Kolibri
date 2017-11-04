package com.ran.kolibri.dto.response.financial

class TransferOperationDto : OperationDto(operationType = TRANSFER_OPERATION_TYPE) {

    var fromAccount: AccountBriefDto? = null
    var fromAccountResultMoneyAmount: Double? = null
    var toAccount: AccountBriefDto? = null
    var toAccountResultMoneyAmount: Double? = null

}
