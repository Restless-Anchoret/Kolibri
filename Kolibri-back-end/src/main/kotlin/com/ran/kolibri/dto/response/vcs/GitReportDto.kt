package com.ran.kolibri.dto.response.vcs

import com.ran.kolibri.dto.response.base.BaseEntityDto
import com.ran.kolibri.entity.vcs.GitReportType
import java.util.*

class GitReportDto : BaseEntityDto() {

    val type: GitReportType? = null
    val message: String? = null
    val exception: String? = null
    val date: Date? = null

}
