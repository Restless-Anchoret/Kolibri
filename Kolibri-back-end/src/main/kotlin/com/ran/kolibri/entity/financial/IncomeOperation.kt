package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class IncomeOperation : Operation() {

    @NotNull
    @ManyToOne
    var incomeAccount: Account? = null

    @NotNull
    var resultMoneyAmount: Double = 0.0

}
