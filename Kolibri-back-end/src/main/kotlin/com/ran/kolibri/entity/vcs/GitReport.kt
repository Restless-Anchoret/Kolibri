package com.ran.kolibri.entity.vcs

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.vcs.GitReportType.SUCCESS
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.persistence.*
import javax.persistence.EnumType.STRING
import javax.persistence.TemporalType.TIMESTAMP

@Entity
class GitReport(
        @Enumerated(STRING)
        var type: GitReportType = SUCCESS,
        @NotEmpty
        var message: String = "",
        var exception: String = "",
        @Column(length = MAX_STACKTRACE_LENGTH)
        var stacktrace: String = "",
        @Temporal(TIMESTAMP)
        var date: Date = Date(),
        var timeInMilliseconds: Long = 0
) : BaseEntity() {

    companion object {
        const val MAX_STACKTRACE_LENGTH = 2000
    }

    @ManyToOne
    var repository: GitRepository? = null

}
