package com.ran.kolibri.dto.financial

import com.ran.kolibri.dto.base.NamedEntityDto
import com.ran.kolibri.dto.other.CommentDto
import java.util.*

class AccountDto : NamedEntityDto() {

    var creationDate: Date? = null
    var currentMoneyAmount: Double? = null
    var isActive: Boolean? = null
    var comments: List<CommentDto>? = null

}