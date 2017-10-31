package com.ran.kolibri.dto.financial

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDto
import com.ran.kolibri.dto.financial.OperationDto.Companion.EXPEND_OPERATION_TYPE
import com.ran.kolibri.dto.financial.OperationDto.Companion.INCOME_OPERATION_TYPE
import com.ran.kolibri.dto.financial.OperationDto.Companion.TRANSFER_OPERATION_TYPE
import com.ran.kolibri.dto.other.CommentDto
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType")
@JsonSubTypes(
        JsonSubTypes.Type(value = IncomeOperationDto::class, name = INCOME_OPERATION_TYPE),
        JsonSubTypes.Type(value = ExpendOperationDto::class, name = EXPEND_OPERATION_TYPE),
        JsonSubTypes.Type(value = TransferOperationDto::class, name = TRANSFER_OPERATION_TYPE))
abstract class OperationDto(
        var operationType: String? = null,
        var moneyAmount: Double? = null,
        var operationDate: Date? = null,
        var operationCategory: OperationCategoryDto? = null,
        var comments: List<CommentDto>? = null
) : BaseEntityDto() {

    companion object {
        const val INCOME_OPERATION_TYPE = "income"
        const val EXPEND_OPERATION_TYPE = "expend"
        const val TRANSFER_OPERATION_TYPE = "transfer"
    }

}