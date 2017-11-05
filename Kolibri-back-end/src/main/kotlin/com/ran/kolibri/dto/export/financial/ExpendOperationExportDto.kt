package com.ran.kolibri.dto.export.financial

class ExpendOperationExportDto : OperationExportDto(operationType = EXPEND_OPERATION_TYPE) {

    var expendAccountId: Long? = null
    var resultMoneyAmount: Double? = null

}
