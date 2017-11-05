package com.ran.kolibri.dto.export.financial

class TransferOperationExportDto : OperationExportDto(operationType = TRANSFER_OPERATION_TYPE) {

    var fromAccountId: Long? = null
    var fromAccountResultMoneyAmount: Double? = null
    var toAccountId: Long? = null
    var toAccountResultMoneyAmount: Double? = null

}
