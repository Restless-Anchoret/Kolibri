package com.ran.kolibri.dto.response.financial

import com.ran.kolibri.dto.response.base.NamedEntityDto
import com.ran.kolibri.dto.response.comment.CommentDto

class OperationCategoryDto : NamedEntityDto() {

    var comments: List<CommentDto>? = null

}
