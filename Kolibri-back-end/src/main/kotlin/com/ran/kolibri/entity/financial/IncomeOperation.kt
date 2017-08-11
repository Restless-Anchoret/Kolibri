package com.ran.kolibri.entity.financial

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class IncomeOperation : Operation() {

    @ManyToOne
    @NotNull
    var incomeAccount: Account? = null

    @NotNull
    var resultMoneyAmount: Double = 0.0

}