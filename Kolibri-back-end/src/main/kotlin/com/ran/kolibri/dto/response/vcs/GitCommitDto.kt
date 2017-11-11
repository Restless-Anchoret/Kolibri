package com.ran.kolibri.dto.response.vcs

import java.time.Instant

class GitCommitDto {

    var number: Int? = null
    var id: String? = null
    var author: String? = null
    var email: String? = null
    var message: String? = null
    var date: Instant? = null
    var link: String? = null

}
