package com.ran.kolibri.dto.financial

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.ran.kolibri.dto.base.BaseEntityDTO
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = IncomeOperationDTO::class, name = "income"),
        JsonSubTypes.Type(value = ExpendOperationDTO::class, name = "expend"),
        JsonSubTypes.Type(value = TransferOperationDTO::class, name = "transfer"))
abstract class OperationDTO : BaseEntityDTO() {

    var moneyAmount: Double? = null
    var operationDate: Date? = null
    var operationCategory: OperationCategoryDTO? = null
    var comment: String? = null

}