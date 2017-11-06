package com.ran.kolibri.dto.response.vcs

import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.project.ProjectBriefDto
import java.util.*

class GitRepositoryDto : NamedEntityDto() {

    val url: String? = null
    val username: String? = null
    val password: String? = null
    val isActive: Boolean? = null
    val daysPerCommit: Int? = null
    val daysForReportsStoring: Int? = null
    val lastCommitDate: Date? = null
    val isErroreuos: Boolean? = null
    var projects: List<ProjectBriefDto>? = null

}
