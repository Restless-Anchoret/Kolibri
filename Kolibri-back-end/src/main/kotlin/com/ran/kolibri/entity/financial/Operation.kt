package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.comment.Comment
import com.ran.kolibri.entity.comment.CommentsHolder
import com.ran.kolibri.entity.project.FinancialProject
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.persistence.InheritanceType.JOINED
import javax.validation.constraints.NotNull

@Entity
@Inheritance(strategy = JOINED)
abstract class Operation(
        @NotNull
        var moneyAmount: Double = 0.0,
        @NotNull
        @Temporal(TemporalType.TIMESTAMP)
        var operationDate: Date = Date()
) : BaseEntity(), CommentsHolder, Cloneable {

    @NotNull
    @ManyToOne
    var project: FinancialProject? = null

    @NotNull
    @ManyToOne
    var operationCategory: OperationCategory? = null

    @OneToMany(cascade = arrayOf(PERSIST, REMOVE))
    @OrderColumn
    override val comments: MutableList<Comment> = ArrayList()

    override fun toString(): String {
        return "Operation(projectId=${project?.id}, moneyAmount=$moneyAmount, " +
                "operationDate=${operationDate.time}, operationCategory=${operationCategory?.id}, " +
                "comments=$comments)"
    }

    override abstract public fun clone(): Operation

}
