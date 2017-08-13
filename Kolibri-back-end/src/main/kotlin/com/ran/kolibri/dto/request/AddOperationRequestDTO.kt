package com.ran.kolibri.dto.request

abstract class AddOperationRequestDTO {

    var operationCategoryId: Long? = null
    var moneyAmount: Double? = null
    var comment: String? = null

}