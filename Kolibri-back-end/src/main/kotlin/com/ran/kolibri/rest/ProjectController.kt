package com.ran.kolibri.rest

import com.ran.kolibri.component.DtoPropertyChecker
import com.ran.kolibri.dto.project.ProjectDTO
import com.ran.kolibri.dto.request.CommentTextDTO
import com.ran.kolibri.dto.request.CreateProjectRequestDTO
import com.ran.kolibri.dto.request.EditNamedEntityRequestDTO
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.service.ProjectService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMethod.*

@RestController
@RequestMapping("/projects")
class ProjectController {

    @Autowired
    lateinit var projectService: ProjectService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade
    @Autowired
    lateinit var dtoPropertyChecker: DtoPropertyChecker

    @RequestMapping(method = arrayOf(GET))
    fun getProjectsPage(@RequestParam(value = "isTemplate", defaultValue = "false") isTemplate: Boolean,
                        @RequestParam(value = "name", required = false) name: String?,
                        pageable: Pageable): Page<ProjectDTO> {
        val projectsPage = projectService.getProjects(isTemplate, name, pageable)
        return orikaMapperFacade.mapAsPage(projectsPage, pageable, ProjectDTO::class.java)
    }

    @RequestMapping(value = "/create-financial", method = arrayOf(POST))
    fun createEmptyFinancialProject(@RequestBody createProjectDto: CreateProjectRequestDTO): ProjectDTO {
        dtoPropertyChecker.checkCreateProjectDto(createProjectDto)
        val project = projectService.createEmptyFinancialProject(createProjectDto.name!!,
                createProjectDto.description!!, createProjectDto.isTemplate!!)
        return orikaMapperFacade.map(project, ProjectDTO::class.java)
    }

    @RequestMapping(value = "/create-from-template/{templateId}", method = arrayOf(POST))
    fun createProjectFromTemplate(@PathVariable("templateId") templateId: Long,
                                  @RequestBody createProjectDto: CreateProjectRequestDTO): ProjectDTO {
        dtoPropertyChecker.checkCreateProjectDto(createProjectDto)
        val project = projectService.createProjectFromTemplate(templateId, createProjectDto.name!!,
                createProjectDto.description!!, createProjectDto.isTemplate!!)
        return orikaMapperFacade.map(project, ProjectDTO::class.java)
    }

    @RequestMapping(value = "{projectId}", method = arrayOf(PUT))
    fun editProject(@PathVariable("projectId") projectId: Long,
                    @RequestBody editProjectDto: EditNamedEntityRequestDTO): ProjectDTO {
        dtoPropertyChecker.checkEditNamedEntityDto(editProjectDto)
        val project = projectService.editProject(projectId, editProjectDto.name!!,
                editProjectDto.description!!)
        return orikaMapperFacade.map(project, ProjectDTO::class.java)
    }

    @RequestMapping(value = "{projectId}", method = arrayOf(DELETE))
    fun deleteProject(@PathVariable("projectId") projectId: Long): ResponseEntity<Any> {
        projectService.deleteProject(projectId)
        return ResponseEntity(OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/comment"), method = arrayOf(POST))
    fun addProjectComment(@PathVariable("projectId") projectId: Long,
                          @RequestBody commentTextDto: CommentTextDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        projectService.addProjectComment(projectId, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/comment/{commentIndex}"), method = arrayOf(PUT))
    fun editProjectComment(@PathVariable("projectId") projectId: Long,
                           @PathVariable("commentIndex") commentIndex: Int,
                           @RequestBody commentTextDto: CommentTextDTO): ResponseEntity<Any> {
        dtoPropertyChecker.checkCommentTextDto(commentTextDto)
        projectService.editProjectComment(projectId, commentIndex, commentTextDto.text!!)
        return ResponseEntity(HttpStatus.OK)
    }

    @RequestMapping(path = arrayOf("/{projectId}/comment/{commentIndex}"), method = arrayOf(DELETE))
    fun removeProjectComment(@PathVariable("projectId") projectId: Long,
                             @PathVariable("commentIndex") commentIndex: Int): ResponseEntity<Any> {
        projectService.removeProjectComment(projectId, commentIndex)
        return ResponseEntity(HttpStatus.OK)
    }

}
