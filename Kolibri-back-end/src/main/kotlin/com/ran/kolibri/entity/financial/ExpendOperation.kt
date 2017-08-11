package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "expend_operation")
class ExpendOperation : Operation() {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "expand_account")
    var expendAccount: Account? = null

    @NotNull
    @Column(name = "result_money_amount")
    var resultMoneyAmount: Double = 0.0

}