package com.ran.kolibri.entity.comment

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.user.User
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Temporal
import javax.persistence.TemporalType.TIMESTAMP
import javax.validation.constraints.NotNull

@Entity
class Comment(
        @NotEmpty
        @Column(length = MAX_COMMENT_LENGTH)
        var text: String,
        @Temporal(TIMESTAMP)
        var date: Date = Date()
) : BaseEntity(), Cloneable {

    companion object {
        const val MAX_COMMENT_LENGTH = 10000
    }

    @NotNull
    @ManyToOne
    var author: User? = null

    override public fun clone(): Comment {
        return Comment(text, date)
    }

}
