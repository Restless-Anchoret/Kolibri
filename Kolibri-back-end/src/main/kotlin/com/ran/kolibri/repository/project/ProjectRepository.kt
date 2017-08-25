package com.ran.kolibri.repository.project

import com.ran.kolibri.entity.project.Project
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification

interface ProjectRepository : BaseProjectRepository<Project> {

    fun findByIsTemplate(isTemplate: Boolean, pageable: Pageable? = null,
                         specification: Specification<Project>? = null): Page<Project>

}
