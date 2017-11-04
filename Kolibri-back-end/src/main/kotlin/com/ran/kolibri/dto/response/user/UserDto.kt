package com.ran.kolibri.dto.response.user

import com.ran.kolibri.dto.response.base.BaseEntityDto
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserStatus

class UserDto : BaseEntityDto() {

    var login: String? = null
    var userRole: UserRole? = null
    var userStatus: UserStatus? = null

}
