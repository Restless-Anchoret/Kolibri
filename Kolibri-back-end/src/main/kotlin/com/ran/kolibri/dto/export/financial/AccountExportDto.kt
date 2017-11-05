package com.ran.kolibri.dto.export.financial

import com.ran.kolibri.dto.export.comment.CommentExportDto
import com.ran.kolibri.dto.response.base.NamedEntityDto
import java.util.*

class AccountExportDto : NamedEntityDto() {

    var creationDate: Date? = null
    var currentMoneyAmount: Double? = null
    var isActive: Boolean? = null
    var comments: List<CommentExportDto>? = null

}
