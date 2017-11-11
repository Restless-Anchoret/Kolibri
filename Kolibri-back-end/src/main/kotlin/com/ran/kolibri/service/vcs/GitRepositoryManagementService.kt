package com.ran.kolibri.service.vcs

import com.ran.kolibri.dto.response.vcs.GitCommitDto
import com.ran.kolibri.entity.vcs.GitReport
import com.ran.kolibri.entity.vcs.GitReportType.ERROR
import com.ran.kolibri.entity.vcs.GitReportType.WARNING
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.FileException
import com.ran.kolibri.service.export.ExportService
import com.ran.kolibri.service.file.FileService
import com.ran.kolibri.service.file.FileService.Companion.REPOS_DIRECTORY
import com.ran.kolibri.service.vcs.JGitService.Companion.CommitResult
import com.ran.kolibri.service.vcs.JGitService.Companion.CommitResult.*
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

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
            return gitRepository
        } catch (ex: GitAPIException) {
            gitRepositoryPersistenceService.removeGitRepositoryById(gitRepository.id!!)
            throw BadRequestException("Unable to clone the repository with the given credentials")
        }
    }

    @Transactional
    fun editGitRepository(id: Long, name: String, description: String, url: String,
                          username: String, password: String, isActive: Boolean,
                          daysPerCommit: Int, daysForReportsStoring: Int): GitRepository {
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(id)
        val isCredentialsModified = (gitRepository.url != url ||
                gitRepository.username != username || gitRepository.password != password)

        gitRepository.name = name
        gitRepository.description = description
        gitRepository.url = url
        gitRepository.username = username
        gitRepository.password = password
        gitRepository.isActive = isActive
        gitRepository.daysPerCommit = daysPerCommit
        gitRepository.daysForReportsStoring = daysForReportsStoring

        try {
            if (isCredentialsModified) {
                jgitService.gitPull(gitRepository)
            }
            gitRepositoryPersistenceService.saveGitRepository(gitRepository)
            return gitRepository
        } catch (ex: GitAPIException) {
            throw BadRequestException("Unable to pull the repository with the given credentials")
        }
    }

    @Transactional
    fun removeGitRepositoryById(id: Long) {
        fileService.deleteDirectoryIfExists(REPOS_DIRECTORY, fileService.getRepositoryDirectoryName(id))
        gitRepositoryPersistenceService.removeGitRepositoryById(id)
    }

    @Transactional
    fun getGitRepositoryCommits(id: Long, pageable: Pageable): Page<GitCommitDto> {
        val gitRepository = gitRepositoryPersistenceService.getGitRepositoryById(id)
        val revCommitsList = jgitService.gitLog(gitRepository, pageable.offset, pageable.pageSize)
        val gitCommitDtoList = revCommitsList.mapIndexed { index, revCommit ->
            val gitCommitDto = GitCommitDto()
            val commitId = ObjectId.toString(revCommit.id)
            gitCommitDto.number = pageable.offset + index + 1
            gitCommitDto.id = commitId
            gitCommitDto.author = revCommit.authorIdent.name
            gitCommitDto.email = revCommit.authorIdent.emailAddress
            gitCommitDto.message = revCommit.shortMessage
            gitCommitDto.date = Instant.ofEpochSecond(revCommit.commitTime.toLong())
            gitCommitDto.link = getCommitLink(gitRepository.url, commitId)
            gitCommitDto
        }
        return PageImpl(gitCommitDtoList, pageable, gitRepository.lastCommitNumber.toLong())
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
            return saveGitReportByCommitResult(start, gitRepository, commitResult)
        } catch (ex: Exception) {
            if (ex is GitAPIException || ex is FileException) {
                return saveGitReportByException(start, gitRepository, ex)
            }
            throw ex
        }
    }

    private fun exportProjectToFile(repositoryId: Long, projectId: Long) {
        val exportedProject = exportService.exportProjectAsString(projectId)
        fileService.writeToFile(exportedProject, fileService.getExportedProjectFileName(projectId),
                REPOS_DIRECTORY, fileService.getRepositoryDirectoryName(repositoryId))
    }

    private fun saveGitReportByCommitResult(start: Instant, gitRepository: GitRepository,
                                            commitResult: CommitResult): GitReport {
        val gitReport = GitReport()
        var commitNumberIncrement = 1
        when (commitResult) {
            COMMITED_SUCCESSFULLY -> {
                gitReport.message = "Commit was performed successfully"
            }
            COMMITED_NOT_COMPLETELY -> {
                gitReport.type = WARNING
                gitReport.message = "Commit was performed with warning: uncommitted changed after commit!"
            }
            NOTHING_TO_COMMIT -> {
                gitReport.message = "Nothing to commit"
                commitNumberIncrement = 0
            }
        }
        gitReport.timeInMilliseconds = gitReport.date.time - start.toEpochMilli()
        gitRepository.lastCommitDate = gitReport.date
        gitRepository.lastCommitNumber = gitRepository.lastCommitNumber + commitNumberIncrement
        gitRepository.isErroreuos = false
        gitRepositoryPersistenceService.saveGitRepository(gitRepository)
        gitRepositoryPersistenceService.saveGitReportToGitRepository(gitReport, gitRepository)
        return gitReport
    }

    private fun saveGitReportByException(start: Instant, gitRepository: GitRepository,
                                         ex: Exception): GitReport {
        val gitReport = GitReport()
        gitReport.type = ERROR
        gitReport.message = ex.message ?: ""
        gitReport.exception = ex.javaClass.name
        gitReport.timeInMilliseconds = gitReport.date.time - start.toEpochMilli()
        gitRepository.isErroreuos = true
        gitRepositoryPersistenceService.saveGitRepository(gitRepository)
        gitRepositoryPersistenceService.saveGitReportToGitRepository(gitReport, gitRepository)
        return gitReport
    }

    private fun getCommitLink(url: String, commitId: String): String {
        URL_PREFIX_TO_COMMIT_LINK_FORMAT_MAP.entries.forEach { (urlPrefix, commitLinkFormat) ->
            if (url.startsWith(urlPrefix)) {
                return commitLinkFormat.format(url, commitId)
            }
        }
        return ""
    }

}
