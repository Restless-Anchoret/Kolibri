package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.other.Comment
import com.ran.kolibri.entity.other.CommentsHolder
import com.ran.kolibri.entity.project.FinancialProject
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Operation : BaseEntity(), CommentsHolder, Cloneable {

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

    @OneToMany(cascade = arrayOf(PERSIST, REMOVE))
    @OrderColumn
    override val comments: MutableList<Comment> = ArrayList()

    override fun toString(): String {
        return "Operation(projectId=${project?.id}, moneyAmount=$moneyAmount, " +
                "operationDate=${operationDate?.time}, operationCategory=${operationCategory?.id}, " +
                "comments=$comments)"
    }

    override abstract public fun clone(): Operation

}
