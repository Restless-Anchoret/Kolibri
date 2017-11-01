package com.ran.kolibri.entity.financial

import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class TransferOperation(
        moneyAmount: Double,
        @NotNull
        var fromAccountResultMoneyAmount: Double,
        @NotNull
        var toAccountResultMoneyAmount: Double,
        operationDate: Date = Date()
) : Operation(moneyAmount, operationDate) {

    @NotNull
    @ManyToOne
    var fromAccount: Account? = null

    @NotNull
    @ManyToOne
    var toAccount: Account? = null

    override fun clone(): TransferOperation {
        return TransferOperation(moneyAmount, fromAccountResultMoneyAmount, toAccountResultMoneyAmount, operationDate)
    }

}
