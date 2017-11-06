package com.ran.kolibri.rest

import com.ran.kolibri.dto.response.vcs.GitCommitDto
import com.ran.kolibri.dto.response.vcs.GitReportDto
import com.ran.kolibri.dto.response.vcs.GitRepositoryDto
import com.ran.kolibri.extension.map
import com.ran.kolibri.extension.mapAsPage
import com.ran.kolibri.component.aspect.annotation.RepositoryId
import com.ran.kolibri.service.vcs.GitRepositoryPersistenceService
import ma.glasnost.orika.MapperFacade
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.*
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/repositories")
class RepositoriesController {

    @Autowired
    lateinit var gitRepositoryPersistenceService: GitRepositoryPersistenceService
    @Autowired
    lateinit var orikaMapperFacade: MapperFacade

    @RequestMapping(method = arrayOf(GET))
    fun getCurrentUserGitRepositoriesPage(pageable: Pageable): Page<GitRepositoryDto> {
        val gitRepositoriesPage = gitRepositoryPersistenceService.getCurrentUserGitRepositoriesPage(pageable)
        return orikaMapperFacade.mapAsPage(gitRepositoriesPage, pageable)
    }

    @RequestMapping(value = "/{repositoryId}", method = arrayOf(GET))
    fun getGitRepository(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): GitRepositoryDto {
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(repositoryId)
        return orikaMapperFacade.map(gitRepository)
    }

    @RequestMapping(method = arrayOf(POST))
    fun addGitRepository(): GitRepositoryDto? {
        return null
    }

    @RequestMapping(value = "/{repositoryId}", method = arrayOf(PUT))
    fun editGitRepository(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): GitRepositoryDto? {
        return null
    }

    @RequestMapping(value = "/{repositoryId}", method = arrayOf(DELETE))
    fun removeGitRepository(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): ResponseEntity<Any> {
        gitRepositoryPersistenceService.removeGitRepositoryById(repositoryId)
        return ResponseEntity(OK)
    }

    @RequestMapping(value = "/{repositoryId}/commit", method = arrayOf(POST))
    fun commitGitRepository(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): GitCommitDto? {
        return null
    }

    @RequestMapping(value = "/{repositoryId}/commits", method = arrayOf(GET))
    fun getGitCommitsPage(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): Page<GitCommitDto>? {
        return null
    }

    @RequestMapping(value = "/{repositoryId}/reports", method = arrayOf(GET))
    fun getGitReportsPage(@PathVariable("repositoryId") @RepositoryId repositoryId: Long,
                          pageable: Pageable): Page<GitReportDto> {
        val gitReports = gitRepositoryPersistenceService.getGitReportsPageByGitRepositoryId(repositoryId, pageable)
        return orikaMapperFacade.mapAsPage(gitReports, pageable)
    }

}
