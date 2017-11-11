package com.ran.kolibri.dto.request.vcs

import com.ran.kolibri.dto.request.common.CreateOrEditNamedEntityRequestDto

class EditGitRepositoryRequestDto : CreateOrEditNamedEntityRequestDto() {

    var url: String? = null
    var username: String? = null
    var password: String? = null
    var isActive: Boolean? = null
    var daysPerCommit: Int? = null
    var daysForReportsStoring: Int? = null

}
