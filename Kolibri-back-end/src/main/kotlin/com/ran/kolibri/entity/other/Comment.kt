package com.ran.kolibri.entity.other

import com.ran.kolibri.entity.base.BaseEntity
import java.util.*
import javax.persistence.Entity
import javax.persistence.Temporal
import javax.persistence.TemporalType
import javax.validation.constraints.NotNull

@Entity
class Comment : BaseEntity(), Cloneable {

    @NotNull
    var text: String = ""

    @Temporal(TemporalType.TIMESTAMP)
    var date: Date? = null

    override public fun clone(): Comment {
        val clonedComment = Comment()
        clonedComment.text = this.text
        clonedComment.date = this.date
        return clonedComment
    }

}
