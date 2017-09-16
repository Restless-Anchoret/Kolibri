package com.ran.kolibri.entity.financial

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.other.Comment
import com.ran.kolibri.entity.other.CommentsHolder
import com.ran.kolibri.entity.project.FinancialProject
import java.util.*
import javax.persistence.*
import javax.persistence.CascadeType.*
import javax.validation.constraints.NotNull

@Entity
class Account : NamedEntity(), CommentsHolder, Cloneable {

    @NotNull
    @ManyToOne
    var project: FinancialProject? = null

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    var creationDate: Date? = null

    @NotNull
    var currentMoneyAmount: Double = 0.0

    @NotNull
    var isActive: Boolean = true

    @OneToMany(cascade = arrayOf(PERSIST, REMOVE))
    @OrderColumn
    override val comments: MutableList<Comment> = ArrayList()

    override public fun clone(): Account {
        val account = Account()
        account.name = this.name
        account.description = this.description
        account.creationDate = this.creationDate
        account.currentMoneyAmount = this.currentMoneyAmount
        account.isActive = this.isActive
        return account
    }

}
