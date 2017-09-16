package com.ran.kolibri.dto.financial

import com.ran.kolibri.dto.base.NamedEntityDTO
import com.ran.kolibri.dto.other.CommentDTO

class OperationCategoryDTO : NamedEntityDTO() {

    var comments: List<CommentDTO>? = null

}
