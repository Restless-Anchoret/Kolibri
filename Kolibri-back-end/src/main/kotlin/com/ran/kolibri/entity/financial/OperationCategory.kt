package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.project.FinancialProject
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotNull

@Entity
@Table(name = "operation_strategy")
class OperationCategory : NamedEntity() {

    @NotNull
    @ManyToOne
    var project: FinancialProject? = null

}