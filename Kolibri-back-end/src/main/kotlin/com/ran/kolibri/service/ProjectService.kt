package com.ran.kolibri.service

import com.ran.kolibri.entity.financial.FinancialProjectSettings
import com.ran.kolibri.entity.project.FinancialProject
import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.exception.InternalServerErrorException
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.financial.FinancialProjectSettingsRepository
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.project.ProjectSpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProjectService {

    @Autowired
    lateinit var financialProjectService: FinancialProjectService
    @Autowired
    lateinit var commentService: CommentService

    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var financialProjectSettingsRepository: FinancialProjectSettingsRepository

    @Transactional
    fun getProjects(isTemplate: Boolean, name: String?, pageable: Pageable?): Page<Project> {
        val specification = Specifications.where(ProjectSpecificationFactory.byIsTemplate<Project>(isTemplate))
                .and(BaseSpecificationFactory.byName<Project>(name))
        return projectRepository.findAll(specification, pageable)
    }

    @Transactional
    fun getProjectById(projectId: Long): Project {
        return projectRepository.findOne(projectId)
                ?: throw NotFoundException("Project was not found")
    }

    @Transactional
    fun createEmptyFinancialProject(name: String, description: String,
                                    isTemplate: Boolean): FinancialProject {
        val financialProject = createProject(FinancialProject(), name, description, isTemplate)
        val settings = FinancialProjectSettings()
        settings.financialProject = financialProject
        financialProjectSettingsRepository.save(settings)
        financialProject.settings = settings
        return financialProject
    }

    private fun <T: Project> createProject(project: T, name: String,
                            description: String, isTemplate: Boolean): T {
        project.name = name
        project.description = description
        project.isTemplate = isTemplate
        projectRepository.save(project)
        return project
    }

    @Transactional
    fun editProject(projectId: Long, name: String, description: String): Project {
        val project = getProjectById(projectId)
        project.name = name
        project.description = description
        projectRepository.save(project)
        return project
    }

    @Transactional
    fun deleteProject(projectId: Long) {
        val project = getProjectById(projectId)
        when (project.javaClass) {
            FinancialProject::class.java -> financialProjectService
                    .deleteFinancialProject(projectId)
            else -> throw InternalServerErrorException("Unknown Template Project type")
        }
    }

    @Transactional
    fun createProjectFromTemplate(templateProjectId: Long, name: String,
                                           description: String, isTemplate: Boolean): Project {
        val templateProject = getProjectById(templateProjectId)
        return when (templateProject.javaClass) {
            FinancialProject::class.java -> financialProjectService
                    .createFinancialProjectFromTemplate(templateProjectId, name, description, isTemplate)
            else -> throw InternalServerErrorException("Unknown Template Project type")
        }
    }

    @Transactional
    fun addProjectComment(projectId: Long, commentText: String) {
        val operation = getProjectById(projectId)
        commentService.addComment(operation, projectRepository, commentText)
    }

    @Transactional
    fun editProjectComment(projectId: Long, commentIndex: Int, commentText: String) {
        val operation = getProjectById(projectId)
        commentService.editComment(operation, commentIndex, commentText)
    }

    @Transactional
    fun removeProjectComment(projectId: Long, commentIndex: Int) {
        val operation = getProjectById(projectId)
        commentService.removeComment(operation, projectRepository, commentIndex)
    }

}
