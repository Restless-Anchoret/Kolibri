package com.ran.kolibri.entity.user

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.user.UserRole.ORDINARY_USER
import com.ran.kolibri.entity.user.UserStatus.ACTIVE
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated

class User : BaseEntity() {

    @NotEmpty
    val login: String = ""

    @NotEmpty
    val passwordHash: String = ""

    @Enumerated(STRING)
    val userRole: UserRole = ORDINARY_USER

    @Enumerated(STRING)
    val userStatus: UserStatus = ACTIVE

}
