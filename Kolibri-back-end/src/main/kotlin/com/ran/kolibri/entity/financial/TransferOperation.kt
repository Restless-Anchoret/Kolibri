package com.ran.kolibri.entity.financial

import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
class TransferOperation : Operation() {

    @ManyToOne
    @NotNull
    var fromAccount: Account? = null

    @NotNull
    var fromAccountResultMoneyAmount: Double = 0.0

    @ManyToOne
    @NotNull
    var toAccount: Account? = null

    @NotNull
    var toAccountResultMoneyAmount: Double = 0.0

}