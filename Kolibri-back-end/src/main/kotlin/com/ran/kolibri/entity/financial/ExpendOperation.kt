package com.ran.kolibri.entity.financial

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class ExpendOperation(
        moneyAmount: Double = 0.0,
        @NotNull
        var resultMoneyAmount: Double = 0.0,
        operationDate: Date = Date()
) : Operation(moneyAmount, operationDate) {

    @NotNull
    @ManyToOne
    var expendAccount: Account? = null

    override fun clone(): ExpendOperation {
        return ExpendOperation(moneyAmount, resultMoneyAmount, operationDate)
    }

}
