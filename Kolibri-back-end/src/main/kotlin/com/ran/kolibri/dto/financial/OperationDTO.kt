package com.ran.kolibri.dto.financial

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDTO
import com.ran.kolibri.dto.financial.OperationDTO.Companion.EXPEND_OPERATION_TYPE
import com.ran.kolibri.dto.financial.OperationDTO.Companion.INCOME_OPERATION_TYPE
import com.ran.kolibri.dto.financial.OperationDTO.Companion.TRANSFER_OPERATION_TYPE
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType")
@JsonSubTypes(
        JsonSubTypes.Type(value = IncomeOperationDTO::class, name = INCOME_OPERATION_TYPE),
        JsonSubTypes.Type(value = ExpendOperationDTO::class, name = EXPEND_OPERATION_TYPE),
        JsonSubTypes.Type(value = TransferOperationDTO::class, name = TRANSFER_OPERATION_TYPE))
abstract class OperationDTO(
        var operationType: String? = null,
        var moneyAmount: Double? = null,
        var operationDate: Date? = null,
        var operationCategory: OperationCategoryDTO? = null,
        var comment: String? = null
) : BaseEntityDTO() {

    companion object {
        const val INCOME_OPERATION_TYPE = "income"
        const val EXPEND_OPERATION_TYPE = "expend"
        const val TRANSFER_OPERATION_TYPE = "transfer"
    }

}