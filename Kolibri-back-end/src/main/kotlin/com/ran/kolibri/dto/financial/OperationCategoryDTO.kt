package com.ran.kolibri.dto.financial

import com.ran.kolibri.dto.base.NamedEntityDto
import com.ran.kolibri.dto.other.CommentDto

class OperationCategoryDto : NamedEntityDto() {

    var comments: List<CommentDto>? = null

}
