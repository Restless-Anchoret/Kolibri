package com.ran.kolibri.entity.financial

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class ExpendOperation : Operation() {

    @ManyToOne
    @NotNull
    var expendAccount: Account? = null

    @NotNull
    var resultMoneyAmount: Double = 0.0

}