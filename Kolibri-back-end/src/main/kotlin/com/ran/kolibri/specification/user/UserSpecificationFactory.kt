package com.ran.kolibri.specification.user

import com.ran.kolibri.entity.user.User
import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.entity.user.UserStatus
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import org.springframework.data.jpa.domain.Specification

object UserSpecificationFactory {

    fun byLogin(login: String?): Specification<User>? {
        return if (login == null) null else Specification { root, _, criteriaBuilder ->
            BaseSpecificationFactory.createLikePredicate(criteriaBuilder, root.get<String>("login"), login)
        }
    }

    fun byUserRole(userRole: UserRole?): Specification<User>? {
        return if (userRole == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<UserRole>("userRole"), userRole)
        }
    }

    fun byUserStatus(userStatus: UserStatus?): Specification<User>? {
        return if (userStatus == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<UserStatus>("userStatus"), userStatus)
        }
    }

}
