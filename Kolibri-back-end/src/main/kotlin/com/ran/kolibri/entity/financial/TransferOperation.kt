package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "transfer_operation")
class TransferOperation : Operation() {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "from_account")
    var fromAccount: Account? = null

    @NotNull
    @Column(name = "from_account_result_money_amount")
    var fromAccountResultMoneyAmount: Double = 0.0

    @NotNull
    @ManyToOne
    @JoinColumn(name = "to_account")
    var toAccount: Account? = null

    @NotNull
    @Column(name = "to_account_result_money_amount")
    var toAccountResultMoneyAmount: Double = 0.0

}