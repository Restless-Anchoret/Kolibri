package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.project.FinancialProject
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Operation : BaseEntity() {

    @NotNull
    @ManyToOne
    var project: FinancialProject? = null

    @NotNull
    var moneyAmount: Double = 0.0

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var operationDate: Date? = null

    @NotNull
    @ManyToOne
    var operationCategory: OperationCategory? = null

    @NotNull
    var comment: String = ""

    override fun toString(): String {
        return "Operation(projectId=${project?.id}, moneyAmount=$moneyAmount, " +
                "operationDate=${operationDate?.time}, operationCategory=${operationCategory?.id}, " +
                "comment='$comment')"
    }

}
