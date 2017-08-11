package com.ran.kolibri.rest

import com.ran.kolibri.component.OrikaMapperFacadeFactory
import com.ran.kolibri.dto.project.ProjectDTO
import com.ran.kolibri.service.ProjectService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var orikaMapperFacadeFactory: OrikaMapperFacadeFactory

    @RequestMapping(method = arrayOf(GET))
    fun getAllProjects(): List<ProjectDTO> {
        val projects = projectService.getAllProjects()
        return orikaMapperFacadeFactory.`object`.mapAsList(projects, ProjectDTO::class.java)
    }

}