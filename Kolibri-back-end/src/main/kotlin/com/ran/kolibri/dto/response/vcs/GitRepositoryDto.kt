package com.ran.kolibri.dto.response.vcs

import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.project.ProjectBriefDto
import java.util.*

class GitRepositoryDto : NamedEntityDto() {

    var url: String? = null
    var username: String? = null
    var password: String? = null
    var isActive: Boolean? = null
    var daysPerCommit: Int? = null
    var daysForReportsStoring: Int? = null
    var lastCommitDate: Date? = null
    var lastCommitNumber: Int? = null
    var isErroreuos: Boolean? = null
    var projects: List<ProjectBriefDto>? = null

}
