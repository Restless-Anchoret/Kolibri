package com.ran.kolibri.dto.user

import com.ran.kolibri.dto.base.BaseEntityDto
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserStatus

class UserDto(
        var login: String? = null,
        var userRole: UserRole? = null,
        var userStatus: UserStatus? = null
) : BaseEntityDto()
