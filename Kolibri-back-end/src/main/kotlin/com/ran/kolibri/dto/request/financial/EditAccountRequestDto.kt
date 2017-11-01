package com.ran.kolibri.dto.request.financial

import com.ran.kolibri.dto.request.common.CreateOrEditNamedEntityRequestDto

class EditAccountRequestDto : CreateOrEditNamedEntityRequestDto() {

    var isActive: Boolean? = null

}
