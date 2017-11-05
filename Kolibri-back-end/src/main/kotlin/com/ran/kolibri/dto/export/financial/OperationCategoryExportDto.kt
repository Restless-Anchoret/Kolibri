package com.ran.kolibri.dto.export.financial

import com.ran.kolibri.dto.export.comment.CommentExportDto
import com.ran.kolibri.dto.response.base.NamedEntityDto

class OperationCategoryExportDto : NamedEntityDto() {

    var comments: List<CommentExportDto>? = null

}
