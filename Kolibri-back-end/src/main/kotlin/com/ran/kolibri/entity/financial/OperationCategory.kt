package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.other.Comment
import com.ran.kolibri.entity.other.CommentsHolder
import com.ran.kolibri.entity.project.FinancialProject
import javax.persistence.CascadeType.*
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OrderColumn
import javax.validation.constraints.NotNull

@Entity
class OperationCategory : NamedEntity(), CommentsHolder {

    @NotNull
    @ManyToOne
    var project: FinancialProject? = null

    @OneToMany(cascade = arrayOf(PERSIST, REMOVE))
    @OrderColumn
    override val comments: MutableList<Comment> = ArrayList()

}
