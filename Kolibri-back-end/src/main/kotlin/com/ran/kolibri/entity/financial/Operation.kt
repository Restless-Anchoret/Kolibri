package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Operation : BaseEntity() {

    @NotNull
    @Column(name = "money_amount")
    var moneyAmount: Double = 0.0

    @NotNull
    @Column(name = "operation_date")
    var operationDate: Date? = null

    @NotNull
    @ManyToOne
    @JoinColumn(name = "operation_strategy")
    var operationCategory: OperationCategory? = null

    @NotNull
    var comment: String = ""

}