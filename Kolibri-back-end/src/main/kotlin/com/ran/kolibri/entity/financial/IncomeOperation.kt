package com.ran.kolibri.entity.financial

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class IncomeOperation(
        moneyAmount: Double,
        @NotNull
        var resultMoneyAmount: Double,
        operationDate: Date = Date()
) : Operation(moneyAmount, operationDate) {

    @NotNull
    @ManyToOne
    var incomeAccount: Account? = null

    override fun clone(): IncomeOperation {
        return IncomeOperation(moneyAmount, resultMoneyAmount, operationDate)
    }

}
