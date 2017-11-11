package com.ran.kolibri.dto.request.vcs

import com.ran.kolibri.dto.request.common.CreateOrEditNamedEntityRequestDto

class CreateGitRepositoryRequestDto : CreateOrEditNamedEntityRequestDto() {

    var url: String? = null
    var username: String? = null
    var password: String? = null

}
