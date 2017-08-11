package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull

@Entity
class Account : NamedEntity() {

    @NotNull
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    var creationDate: Date? = null

    @NotNull
    @Column(name = "current_money_amount")
    var currentMoneyAmount: Double = 0.0

    @NotNull
    @Column(name = "is_active")
    var isActive: Boolean = true

}