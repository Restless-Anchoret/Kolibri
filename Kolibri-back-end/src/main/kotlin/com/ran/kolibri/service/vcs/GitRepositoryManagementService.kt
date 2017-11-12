package com.ran.kolibri.service.vcs

import com.ran.kolibri.dto.response.vcs.GitCommitDto
import com.ran.kolibri.entity.vcs.GitReport
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.FileException
import com.ran.kolibri.extension.logError
import com.ran.kolibri.service.export.ExportService
import com.ran.kolibri.service.file.FileService
import com.ran.kolibri.service.file.FileService.Companion.REPOS_DIRECTORY
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

@Service
class GitRepositoryManagementService {

    companion object {
        private val URL_PREFIX_TO_COMMIT_LINK_FORMAT_MAP = mapOf(
                "https://github.com/" to "%s/commit/%s",
                "https://bitbucket.org/" to "%s/commits/%s")
    }

    @Autowired
    lateinit var gitRepositoryPersistenceService: GitRepositoryPersistenceService
    @Autowired
    lateinit var jgitService: JGitService
    @Autowired
    lateinit var fileService: FileService
    @Autowired
    lateinit var exportService: ExportService

    @Transactional
    fun createGitRepository(name: String, description: String, url: String,
                            username: String, password: String): GitRepository {
        val gitRepository = gitRepositoryPersistenceService.createGitRepository(
                name, description, url, username, password)
        try {
            jgitService.gitClone(gitRepository)
            return gitRepositoryPersistenceService.saveGitRepositoryLastCommitNumber(
                    gitRepository, jgitService.gitLog(gitRepository).size)
        } catch (ex: Exception) {
            logError(ex) { "Exception while trying to create Git Repository" }
            val extractedException = tryToExtractException(ex, GitAPIException::class.java)
            if (extractedException != null) {
                gitRepositoryPersistenceService.removeGitRepositoryById(gitRepository.id!!)
                throw BadRequestException("Unable to access the repository with the given credentials", extractedException)
            }
            throw ex
        }
    }

    @Transactional
    fun editGitRepository(id: Long, name: String, description: String,
                          username: String, password: String, isActive: Boolean,
                          daysPerCommit: Int, daysForReportsStoring: Int,
                          projectIds: List<Long>): GitRepository {
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(id)

        if (gitRepository.username != username || gitRepository.password != password) {
            try {
                gitRepository.username = username
                gitRepository.password = password
                jgitService.gitPull(gitRepository)
            } catch (ex: Exception) {
                logError(ex) { "Exception while trying to edit Git Repository" }
                val extractedException = tryToExtractException(ex, GitAPIException::class.java)
                if (extractedException != null) {
                    throw BadRequestException("Unable to access the repository with the given credentials", extractedException)
                }
                throw ex
            }
        }

        return gitRepositoryPersistenceService.saveGitRepository(gitRepository, name, description,
                username, password, isActive, daysPerCommit, daysForReportsStoring, projectIds)
    }

    @Transactional
    fun removeGitRepositoryById(id: Long) {
        fileService.deleteDirectoryIfExists(REPOS_DIRECTORY, fileService.getRepositoryDirectoryName(id))
        gitRepositoryPersistenceService.removeGitRepositoryById(id)
    }

    @Transactional
    fun getGitRepositoryCommits(id: Long, pageable: Pageable): Page<GitCommitDto> {
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(id)
        val totalCommitsQuantity = gitRepository.lastCommitNumber
        val revCommitsList = jgitService.gitLog(gitRepository, pageable.offset, pageable.pageSize)
        val gitCommitDtoList = revCommitsList.mapIndexed { index, revCommit ->
            val gitCommitDto = GitCommitDto()
            val commitId = ObjectId.toString(revCommit.id)
            gitCommitDto.number = totalCommitsQuantity - (pageable.offset + index)
            gitCommitDto.id = commitId
            gitCommitDto.author = revCommit.authorIdent.name
            gitCommitDto.email = revCommit.authorIdent.emailAddress
            gitCommitDto.message = revCommit.shortMessage
            gitCommitDto.date = Date(revCommit.commitTime.toLong() * 1000)
            gitCommitDto.link = getCommitLink(gitRepository.url, commitId)
            gitCommitDto
        }
        return PageImpl(gitCommitDtoList, pageable, totalCommitsQuantity.toLong())
    }

    @Transactional
    fun commitGitRepository(id: Long): GitReport {
        val start = Instant.now()
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(id)
        try {
            jgitService.gitPull(gitRepository)
            gitRepository.projects.forEach { project ->
                exportProjectToFile(gitRepository.id!!, project.id!!)
            }
            val commitResult = jgitService.gitCommit(gitRepository)
            jgitService.gitPush(gitRepository)
            return gitRepositoryPersistenceService.saveGitReportByCommitResult(start, gitRepository, commitResult)
        } catch (ex: Exception) {
            logError(ex) { "Exception while performing Git commit" }
            val extractedException = tryToExtractException(ex, FileException::class.java, GitAPIException::class.java)
            if (extractedException != null) {
                extractedException as Exception
                return gitRepositoryPersistenceService.saveGitReportByException(start, gitRepository, extractedException)
            }
            throw ex
        }
    }

    private fun exportProjectToFile(repositoryId: Long, projectId: Long) {
        val exportedProject = exportService.exportProjectAsString(projectId)
        fileService.writeToFile(exportedProject, fileService.getExportedProjectFileName(projectId),
                REPOS_DIRECTORY, fileService.getRepositoryDirectoryName(repositoryId))
    }

    private fun getCommitLink(url: String, commitId: String): String {
        URL_PREFIX_TO_COMMIT_LINK_FORMAT_MAP.entries.forEach { (urlPrefix, commitLinkFormat) ->
            if (url.startsWith(urlPrefix)) {
                return commitLinkFormat.format(url, commitId)
            }
        }
        return ""
    }

    private fun tryToExtractException(ex: Throwable, vararg expectedExceptionClasses: Class<out Throwable>): Throwable? {
        if (expectedExceptionClasses.any { exceptionClass -> exceptionClass.isAssignableFrom(ex.javaClass) }) {
            return ex
        }
        if (expectedExceptionClasses.any { exceptionClass -> exceptionClass.isAssignableFrom(ex.cause?.javaClass) }) {
            return ex.cause
        }
        return null
    }

}
