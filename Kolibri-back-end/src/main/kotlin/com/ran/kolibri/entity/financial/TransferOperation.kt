package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class TransferOperation : Operation() {

    @NotNull
    @ManyToOne
    var fromAccount: Account? = null

    @NotNull
    var fromAccountResultMoneyAmount: Double = 0.0

    @NotNull
    @ManyToOne
    var toAccount: Account? = null

    @NotNull
    var toAccountResultMoneyAmount: Double = 0.0

}
