package com.ran.kolibri.dto.financial

class TransferOperationDTO: OperationDTO() {

    var fromAccount: AccountDTO? = null
    var fromAccountResultMoneyAmount: Double? = null
    var toAccount: AccountDTO? = null
    var toAccountResultMoneyAmount: Double? = null

}