package com.ran.kolibri.dto.response.vcs

import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.project.ProjectBriefDto
import com.ran.kolibri.dto.response.user.UserBriefDto
import java.util.*

class VcsRepositoryDto : NamedEntityDto() {

    val url: String? = null
    val isActive: Boolean? = null
    val daysPerCommit: Int? = null
    val lastCommitDate: Date? = null
    var owner: UserBriefDto? = null
    var projects: List<ProjectBriefDto>? = null

}
