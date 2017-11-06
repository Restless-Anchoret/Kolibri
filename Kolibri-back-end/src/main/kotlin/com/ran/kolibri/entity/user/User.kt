package com.ran.kolibri.entity.user

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.user.UserRole.ORDINARY_USER
import com.ran.kolibri.entity.user.UserStatus.ACTIVE
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType.STRING
import javax.persistence.Enumerated
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(
        @NotEmpty
        @Column(unique = true)
        val login: String = "",
        @NotEmpty
        var passwordHash: String = "",
        @Enumerated(STRING)
        val userRole: UserRole = ORDINARY_USER,
        @Enumerated(STRING)
        var userStatus: UserStatus = ACTIVE
) : BaseEntity()
