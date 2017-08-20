package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class ExpendOperation : Operation() {

    @NotNull
    @ManyToOne
    var expendAccount: Account? = null

    @NotNull
    var resultMoneyAmount: Double = 0.0

}
