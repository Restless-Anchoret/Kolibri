package com.ran.kolibri.specification.vcs

import com.ran.kolibri.entity.user.User
import com.ran.kolibri.entity.vcs.GitRepository
import org.springframework.data.jpa.domain.Specification

object GitRepositorySpecificationFactory {

    fun byOwnerId(ownerId: Long?): Specification<GitRepository>? {
        return if (ownerId == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<User>("owner").get<Long>("id"), ownerId)
        }
    }

    fun byIsActive(isActive: Boolean?): Specification<GitRepository>? {
        return if (isActive == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Boolean>("isActive"), isActive)
        }
    }

}
