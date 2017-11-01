package com.ran.kolibri.dto.request.user

import com.ran.kolibri.entity.user.UserStatus

data class EditUserRequestDto(
        var userStatus: UserStatus? = null
)
