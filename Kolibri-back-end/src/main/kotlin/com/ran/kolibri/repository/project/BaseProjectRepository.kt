package com.ran.kolibri.repository.project

import com.ran.kolibri.entity.project.Project
import org.springframework.data.repository.CrudRepository

interface BaseProjectRepository<T: Project> : CrudRepository<T, Long>