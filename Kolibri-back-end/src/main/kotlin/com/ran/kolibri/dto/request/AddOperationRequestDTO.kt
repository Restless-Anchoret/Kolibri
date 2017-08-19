package com.ran.kolibri.dto.request

import java.util.*

abstract class AddOperationRequestDTO {

    var operationCategoryId: Long? = null
    var moneyAmount: Double? = null
    var comment: String? = null
    var operationDate: Date? = null

}
