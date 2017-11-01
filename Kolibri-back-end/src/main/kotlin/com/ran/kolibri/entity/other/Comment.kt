package com.ran.kolibri.entity.other

import com.ran.kolibri.entity.base.BaseEntity
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.persistence.Entity
import javax.persistence.Temporal
import javax.persistence.TemporalType.TIMESTAMP

@Entity
class Comment(
        @NotEmpty
        var text: String,
        @Temporal(TIMESTAMP)
        var date: Date = Date()
) : BaseEntity(), Cloneable {

    override public fun clone(): Comment {
        return Comment(text, date)
    }

}
