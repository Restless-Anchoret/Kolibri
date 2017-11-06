package com.ran.kolibri.entity.vcs

import com.ran.kolibri.entity.base.NamedEntity
import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.entity.user.User
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.ManyToOne
import javax.persistence.Temporal
import javax.persistence.TemporalType.TIMESTAMP
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import kotlin.collections.ArrayList

@Entity
class GitRepository(
        name: String = "",
        description: String = "",
        @NotEmpty
        val url: String = "",
        @NotEmpty
        val username: String = "",
        @NotEmpty
        val password: String = "",
        @NotNull
        val isActive: Boolean = false,
        @NotNull
        @Min(1)
        val daysPerCommit: Int = 7,
        @NotNull
        @Min(1)
        val daysForReportsStoring: Int = 30,
        @Temporal(TIMESTAMP)
        val lastCommitDate: Date? = null,
        @NotNull
        val isErroreuos: Boolean = false
): NamedEntity(name, description) {

    @NotNull
    @ManyToOne
    var owner: User? = null

    @ManyToMany
    val projects: MutableList<Project> = ArrayList()

}
