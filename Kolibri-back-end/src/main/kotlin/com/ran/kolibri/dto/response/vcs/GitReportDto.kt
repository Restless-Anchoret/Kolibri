package com.ran.kolibri.dto.response.vcs

import com.ran.kolibri.dto.response.base.BaseEntityDto
import com.ran.kolibri.entity.vcs.GitReportType
import java.util.*

class GitReportDto : BaseEntityDto() {

    var type: GitReportType? = null
    var message: String? = null
    var exception: String? = null
    var date: Date? = null
    var timeInMilliseconds: Long? = null

}
