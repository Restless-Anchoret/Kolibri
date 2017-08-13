package com.ran.kolibri.service

import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.project.FinancialProjectRepository
import com.ran.kolibri.repository.project.ProjectRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService {

    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var financialProjectRepository: FinancialProjectRepository

    @Transactional
    fun getAllProjects(): List<Project> {
        return projectRepository.findAll().toList()
    }

    @Transactional
    fun getFinancialProjectById(projectId: Long): FinancialProject {
        return financialProjectRepository.findOne(projectId)
                ?: throw NotFoundException("Project was not found for id: $projectId")
    }

}