package com.ran.kolibri.dto.response.financial

import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.comment.CommentDto
import java.util.*

class AccountDto : NamedEntityDto() {

    var creationDate: Date? = null
    var currentMoneyAmount: Double? = null
    var isActive: Boolean? = null
    var comments: List<CommentDto>? = null

}