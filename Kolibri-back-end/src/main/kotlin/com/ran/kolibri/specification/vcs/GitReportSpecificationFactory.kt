package com.ran.kolibri.specification.vcs

import com.ran.kolibri.entity.vcs.GitReport
import com.ran.kolibri.entity.vcs.GitRepository
import org.springframework.data.jpa.domain.Specification

object GitReportSpecificationFactory {

    fun byGitRepositoryId(repositoryId: Long): Specification<GitReport> {
        return Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<GitRepository>("repository").get<Long>("id"), repositoryId)
        }
    }

}
