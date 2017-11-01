package com.ran.kolibri.entity.user

import com.ran.kolibri.entity.base.BaseEntity
import com.ran.kolibri.entity.user.UserRole.ORDINARY_USER
import com.ran.kolibri.entity.user.UserStatus.ACTIVE
import com.ran.kolibri.entity.vcs.VcsRepository
import org.hibernate.validator.constraints.NotEmpty
import javax.persistence.*
import javax.persistence.EnumType.STRING

@Entity
@Table(name = "users")
class User(
        @NotEmpty
        @Column(unique = true)
        val login: String = "",
        @NotEmpty
        val passwordHash: String = "",
        @Enumerated(STRING)
        val userRole: UserRole = ORDINARY_USER,
        @Enumerated(STRING)
        var userStatus: UserStatus = ACTIVE
) : BaseEntity() {

    @OneToMany(mappedBy = "owner")
    val vcsRepositories: MutableList<VcsRepository> = ArrayList()

}
