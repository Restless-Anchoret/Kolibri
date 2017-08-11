package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Inheritance
import javax.persistence.InheritanceType
import javax.persistence.ManyToOne
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Operation : BaseEntity() {

    @NotNull
    var moneyAmount: Double = 0.0

    @NotNull
    var resultMoneyAmount: Double = 0.0

    @NotNull
    var operationDate: Date? = null

    @ManyToOne
    @NotNull
    var operationCategory: OperationCategory? = null

}