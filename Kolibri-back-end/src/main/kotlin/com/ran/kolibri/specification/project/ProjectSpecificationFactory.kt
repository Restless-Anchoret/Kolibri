package com.ran.kolibri.specification.project

import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.entity.user.User
import org.springframework.data.jpa.domain.Specification

object ProjectSpecificationFactory {

    fun <T: Project> byIsAccessible(user: User): Specification<T> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.or(
                    criteriaBuilder.equal(root.get<User>("owner"), user),
                    criteriaBuilder.isMember(user, root.get<MutableList<User>>("usersWithAccess")))
        }
    }

    fun <T: Project> byIsTemplate(isTemplate: Boolean?): Specification<T>? {
        return if (isTemplate == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Boolean>("isTemplate"), isTemplate)
        }
    }

}
