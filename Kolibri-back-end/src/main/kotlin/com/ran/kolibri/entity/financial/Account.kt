package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull

@Entity
class Account : NamedEntity() {

    @Temporal(TemporalType.TIMESTAMP)
    @NotNull
    var creationDate: Date? = null

    @NotNull
    var currentMoneyAmount: Double = 0.0

    @NotNull
    var isActive: Boolean = true

}