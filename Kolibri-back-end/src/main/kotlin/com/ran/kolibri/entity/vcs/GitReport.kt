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
        val type: GitReportType = SUCCESS,
        @NotEmpty
        val message: String = "",
        val exception: String? = null,
        @Temporal(TIMESTAMP)
        val date: Date = Date()
) : BaseEntity() {

    @ManyToOne
    var repository: GitRepository? = null

}
