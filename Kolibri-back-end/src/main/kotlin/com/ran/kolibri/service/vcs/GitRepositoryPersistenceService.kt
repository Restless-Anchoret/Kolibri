package com.ran.kolibri.service.vcs

import com.ran.kolibri.entity.vcs.GitReport
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.exception.NotFoundException
import com.ran.kolibri.repository.vcs.GitReportRepository
import com.ran.kolibri.repository.vcs.GitRepositoryRepository
import com.ran.kolibri.service.user.UserService
import com.ran.kolibri.specification.vcs.GitReportSpecificationFactory
import com.ran.kolibri.specification.vcs.GitRepositorySpecificationFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specifications
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GitRepositoryPersistenceService {

    @Autowired
    lateinit var gitRepositoryRepository: GitRepositoryRepository
    @Autowired
    lateinit var gitReportRepository: GitReportRepository
    @Autowired
    lateinit var userService: UserService

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
        val gitRepository = GitRepository(name, description, url, username, password)
        gitRepositoryRepository.save(gitRepository)
        return gitRepository
    }

    @Transactional
    fun saveGitRepository(gitRepository: GitRepository) {
        gitRepositoryRepository.save(gitRepository)
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
    fun saveGitReportToGitRepository(gitReport: GitReport, gitRepository: GitRepository) {
        gitReport.repository = gitRepository
        gitReportRepository.save(gitReport)
    }

}
