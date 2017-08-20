package com.ran.kolibri.rest

import com.ran.kolibri.dto.project.ProjectDTO
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.service.ProjectService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade

    @RequestMapping(method = arrayOf(GET))
    fun getActiveProjectsPage(pageable: Pageable): Page<ProjectDTO> {
        val projectsPage = projectService.getAllActiveProjects(pageable)
        return orikaMapperFacade.mapAsPage(projectsPage, pageable, ProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/all"), method = arrayOf(GET))
    fun getAllActiveProjects(): List<ProjectDTO> {
        val projects = projectService.getAllActiveProjects(null).content
        return orikaMapperFacade.mapAsList(projects, ProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/templates"), method = arrayOf(GET))
    fun getTemplateProjectsPage(pageable: Pageable): Page<ProjectDTO> {
        val projectsPage = projectService.getAllTemplateProjects(pageable)
        return orikaMapperFacade.mapAsPage(projectsPage, pageable, ProjectDTO::class.java)
    }

    @RequestMapping(path = arrayOf("/templates/all"), method = arrayOf(GET))
    fun getAllTemplateProjects(): List<ProjectDTO> {
        val projects = projectService.getAllTemplateProjects(null)
        return orikaMapperFacade.mapAsList(projects, ProjectDTO::class.java)
    }

}
