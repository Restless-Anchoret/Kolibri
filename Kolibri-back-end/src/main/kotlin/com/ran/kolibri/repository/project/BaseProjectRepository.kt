package com.ran.kolibri.repository.project

import com.ran.kolibri.entity.project.Project
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface BaseProjectRepository<T: Project> : CrudRepository<T, Long>