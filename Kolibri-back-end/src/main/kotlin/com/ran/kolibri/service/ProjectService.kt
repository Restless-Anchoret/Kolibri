package com.ran.kolibri.service

import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.repository.project.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ProjectService {

    @Autowired
    lateinit var projectRepository: ProjectRepository

    fun getAllProjects(): List<Project> {
        return projectRepository.findAll().toList()
    }

}