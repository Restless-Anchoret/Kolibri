package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import javax.persistence.Entity
import javax.validation.constraints.NotNull

@Entity
class Account : NamedEntity() {

    @NotNull
    var currentMoneyAmount: Double = 0.0

}