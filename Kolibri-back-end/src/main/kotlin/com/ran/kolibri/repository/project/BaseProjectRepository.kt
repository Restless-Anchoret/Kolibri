package com.ran.kolibri.repository.project

import com.ran.kolibri.entity.project.Project
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseProjectRepository<T: Project> : JpaRepository<T, Long>, JpaSpecificationExecutor<T>
