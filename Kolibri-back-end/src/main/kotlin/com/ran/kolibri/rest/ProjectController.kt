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
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/projects")
class ProjectController {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade

    @RequestMapping(method = arrayOf(GET))
    fun getProjectsPage(@RequestParam(value = "isTemplate", defaultValue = "false") isTemplate: Boolean,
                        @RequestParam(value = "name", required = false) name: String?,
                        pageable: Pageable): Page<ProjectDTO> {
        val projectsPage = projectService.getProjects(isTemplate, name, pageable)
        return orikaMapperFacade.mapAsPage(projectsPage, pageable, ProjectDTO::class.java)
    }

}
