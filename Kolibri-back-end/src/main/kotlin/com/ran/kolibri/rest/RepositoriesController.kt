package com.ran.kolibri.rest

import com.ran.kolibri.dto.response.vcs.GitCommitDto
import com.ran.kolibri.dto.response.vcs.GitReportDto
import com.ran.kolibri.dto.response.vcs.GitRepositoryDto
import com.ran.kolibri.security.annotation.RepositoryId
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

    @RequestMapping(method = arrayOf(GET))
    fun getCurrentUserGitRepositoriesPage(pageable: Pageable): Page<GitRepositoryDto>? {
        return null
    }

    @RequestMapping(value = "/{repositoryId}", method = arrayOf(GET))
    fun getGitRepository(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): GitRepositoryDto? {
        return null
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
    fun getGitReportsPage(@PathVariable("repositoryId") @RepositoryId repositoryId: Long): Page<GitReportDto>? {
        return null
    }

}
