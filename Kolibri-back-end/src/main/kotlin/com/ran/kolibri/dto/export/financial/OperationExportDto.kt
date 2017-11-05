package com.ran.kolibri.dto.export.financial

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.export.comment.CommentExportDto
import com.ran.kolibri.dto.export.financial.OperationExportDto.Companion.EXPEND_OPERATION_TYPE
import com.ran.kolibri.dto.export.financial.OperationExportDto.Companion.INCOME_OPERATION_TYPE
import com.ran.kolibri.dto.export.financial.OperationExportDto.Companion.TRANSFER_OPERATION_TYPE
import com.ran.kolibri.dto.response.base.BaseEntityDto
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "operationType")
@JsonSubTypes(
        Type(value = IncomeOperationExportDto::class, name = INCOME_OPERATION_TYPE),
        Type(value = ExpendOperationExportDto::class, name = EXPEND_OPERATION_TYPE),
        Type(value = TransferOperationExportDto::class, name = TRANSFER_OPERATION_TYPE))
abstract class OperationExportDto(
        var operationType: String? = null
) : BaseEntityDto() {

    companion object {
        const val INCOME_OPERATION_TYPE = "income"
        const val EXPEND_OPERATION_TYPE = "expend"
        const val TRANSFER_OPERATION_TYPE = "transfer"
    }

    var moneyAmount: Double? = null
    var operationDate: Date? = null
    var operationCategoryId: Long? = null
    var comments: List<CommentExportDto>? = null

}
