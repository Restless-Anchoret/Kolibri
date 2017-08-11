package com.ran.kolibri.entity.financial

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "income_operation")
class IncomeOperation : Operation() {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "income_account")
    var incomeAccount: Account? = null

    @NotNull
    @Column(name = "result_money_amount")
    var resultMoneyAmount: Double = 0.0

}