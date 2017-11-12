package com.ran.kolibri.service.vcs

import com.ran.kolibri.entity.project.Project
import com.ran.kolibri.entity.vcs.GitReport
import com.ran.kolibri.entity.vcs.GitReport.Companion.MAX_STACKTRACE_LENGTH
import com.ran.kolibri.entity.vcs.GitReportType
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.repository.vcs.GitReportRepository
import com.ran.kolibri.repository.vcs.GitRepositoryRepository
import com.ran.kolibri.service.user.UserService
import com.ran.kolibri.service.vcs.JGitCommitResult.*
import com.ran.kolibri.specification.base.BaseSpecificationFactory
import com.ran.kolibri.specification.project.ProjectSpecificationFactory
import com.ran.kolibri.specification.vcs.GitReportSpecificationFactory
import com.ran.kolibri.specification.vcs.GitRepositorySpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.PrintWriter
import java.io.StringWriter
import java.time.Instant


@Service
class GitRepositoryPersistenceService {

    @Autowired
    lateinit var gitRepositoryRepository: GitRepositoryRepository
    @Autowired
    lateinit var gitReportRepository: GitReportRepository
    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var projectRepository: ProjectRepository

    @Transactional
    fun getGitRepositoryById(id: Long): GitRepository {
        return gitRepositoryRepository.findOne(id) ?:
                throw NotFoundException("Git Repository not found")
    }

    @Transactional
    fun getGitRepositoriesPage(pageable: Pageable, ownerId: Long? = null,
                               isActive: Boolean? = null): Page<GitRepository> {
        val specification = Specifications.where(GitRepositorySpecificationFactory.byOwnerId(ownerId))
                .and(GitRepositorySpecificationFactory.byIsActive(isActive))
        return gitRepositoryRepository.findAll(specification, pageable)
    }

    @Transactional
    fun getCurrentUserGitRepositoriesPage(pageable: Pageable, isActive: Boolean? = null): Page<GitRepository> {
        val currentUserId = userService.getAuthenticatedUserData().id
        return getGitRepositoriesPage(pageable, currentUserId, isActive)
    }

    @Transactional
    fun createGitRepository(name: String, description: String, url: String,
                            username: String, password: String): GitRepository {
        val currentUser = userService.getAuthenticatedUser()
        val gitRepository = GitRepository(name, description, url, username, password)
        gitRepository.owner = currentUser
        gitRepositoryRepository.save(gitRepository)
        return gitRepository
    }

    @Transactional
    fun saveGitRepository(gitRepository: GitRepository, name: String, description: String,
                          username: String, password: String, isActive: Boolean,
                          daysPerCommit: Int, daysForReportsStoring: Int, projectIds: List<Long>): GitRepository {
        val currentUser = userService.getAuthenticatedUser()
        val projectsSpecification = Specifications.where(BaseSpecificationFactory.byInIds<Project>(projectIds))
                .and(ProjectSpecificationFactory.byIsAccessible(currentUser))
        val projects = projectRepository.findAll(projectsSpecification)
        gitRepository.projects.clear()
        gitRepository.projects.addAll(projects)
        gitRepository.name = name
        gitRepository.description = description
        gitRepository.username = username
        gitRepository.password = password
        gitRepository.isActive = isActive
        gitRepository.daysPerCommit = daysPerCommit
        gitRepository.daysForReportsStoring = daysForReportsStoring
        gitRepositoryRepository.save(gitRepository)
        return gitRepository
    }

    @Transactional
    fun saveGitRepositoryLastCommitNumber(gitRepository: GitRepository, lastCommitNumber: Int): GitRepository {
        gitRepository.lastCommitNumber = lastCommitNumber
        gitRepositoryRepository.save(gitRepository)
        return gitRepository
    }

    @Transactional
    fun removeGitRepositoryById(id: Long) {
        gitRepositoryRepository.delete(id)
    }

    @Transactional
    fun getGitReportsPageByGitRepositoryId(repositoryId: Long, pageable: Pageable): Page<GitReport> {
        val specification = GitReportSpecificationFactory.byGitRepositoryId(repositoryId)
        return gitReportRepository.findAll(specification, pageable)
    }

    @Transactional
    fun saveGitReportByCommitResult(start: Instant, gitRepository: GitRepository,
                                    commitResult: JGitCommitResult): GitReport {
        val gitReport = GitReport()
        var commitNumberIncrement = 1
        when (commitResult) {
            COMMITTED_SUCCESSFULLY -> {
                gitReport.message = "Commit was performed successfully"
            }
            COMMITTED_NOT_COMPLETELY -> {
                gitReport.type = GitReportType.WARNING
                gitReport.message = "Commit was performed with warning: uncommitted changed after commit!"
            }
            NOTHING_TO_COMMIT -> {
                gitReport.message = "Nothing to commit"
                commitNumberIncrement = 0
            }
        }
        gitReport.timeInMilliseconds = gitReport.date.time - start.toEpochMilli()
        gitReport.repository = gitRepository
        gitRepository.lastCommitDate = gitReport.date
        gitRepository.lastCommitNumber = gitRepository.lastCommitNumber + commitNumberIncrement
        gitRepository.isErroreuos = false
        gitRepositoryRepository.save(gitRepository)
        gitReportRepository.save(gitReport)
        return gitReport
    }

    @Transactional
    fun saveGitReportByException(start: Instant, gitRepository: GitRepository,
                                         ex: Exception): GitReport {
        val gitReport = GitReport()
        gitReport.type = GitReportType.ERROR
        gitReport.message = ex.message ?: ""
        gitReport.exception = ex.javaClass.name
        gitReport.stacktrace = getStackTraceAsString(ex)
        gitReport.timeInMilliseconds = gitReport.date.time - start.toEpochMilli()
        gitReport.repository = gitRepository
        gitRepository.isErroreuos = true
        gitRepositoryRepository.save(gitRepository)
        gitReportRepository.save(gitReport)
        return gitReport
    }

    private fun getStackTraceAsString(ex: Exception): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        ex.printStackTrace(pw)
        val fullStacktrace = sw.toString()
        return if (fullStacktrace.length <= MAX_STACKTRACE_LENGTH) {
            fullStacktrace
        } else {
            fullStacktrace.substring(0, MAX_STACKTRACE_LENGTH - 3) + "..."
        }
    }

}
