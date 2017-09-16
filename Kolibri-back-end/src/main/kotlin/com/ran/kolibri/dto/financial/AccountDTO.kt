package com.ran.kolibri.dto.financial

import com.ran.kolibri.dto.base.NamedEntityDTO
import com.ran.kolibri.dto.other.CommentDTO
import java.util.*

class AccountDTO : NamedEntityDTO() {

    var creationDate: Date? = null
    var currentMoneyAmount: Double? = null
    var isActive: Boolean? = null
    var comments: List<CommentDTO>? = null

}