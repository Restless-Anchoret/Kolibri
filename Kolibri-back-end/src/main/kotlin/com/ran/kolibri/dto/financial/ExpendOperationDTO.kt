package com.ran.kolibri.dto.financial

import com.ran.kolibri.entity.financial.Account

class ExpendOperationDTO: OperationDTO() {

    var expendAccount: Account? = null
    var resultMoneyAmount: Double? = null

}