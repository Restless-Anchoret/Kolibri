package com.ran.kolibri.specification.project

import com.ran.kolibri.entity.project.Project
import org.springframework.data.jpa.domain.Specification

object ProjectSpecificationFactory {

    fun <T: Project> byIsTemplate(isTemplate: Boolean?): Specification<T>? {
        return if (isTemplate == null) null else Specification { root, _, criteriaBuilder ->
            criteriaBuilder.equal(root.get<Boolean>("isTemplate"), isTemplate)
        }
    }

}
