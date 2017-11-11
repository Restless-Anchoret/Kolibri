package com.ran.kolibri.service.vcs

import com.ran.kolibri.app.Config.VCS_COMMIT_AUTHOR
import com.ran.kolibri.app.Config.VCS_COMMIT_EMAIL
import com.ran.kolibri.app.Config.VCS_COMMIT_MESSAGE
import com.ran.kolibri.component.aspect.annotation.FileExceptionWrap
import com.ran.kolibri.entity.vcs.GitRepository
import com.ran.kolibri.service.file.FileService
import com.ran.kolibri.service.file.FileService.Companion.REPOS_DIRECTORY
import com.ran.kolibri.service.vcs.JGitService.Companion.CommitResult.*
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JGitService {

    companion object {
        enum class CommitResult {
            COMMITED_SUCCESSFULLY,
            COMMITED_NOT_COMPLETELY,
            NOTHING_TO_COMMIT
        }
    }

    @Autowired
    lateinit var fileService: FileService

    @Value(VCS_COMMIT_AUTHOR)
    lateinit var commitAuthor: String
    @Value(VCS_COMMIT_EMAIL)
    lateinit var commitEmail: String
    @Value(VCS_COMMIT_MESSAGE)
    lateinit var commitMessage: String

    @FileExceptionWrap
    fun gitClone(repository: GitRepository) {
        val pathArray = arrayOf(REPOS_DIRECTORY, repository.id.toString())
        try {
            fileService.deleteDirectoryIfExists(*pathArray)
            fileService.createDirectoryIfNotExists(*pathArray)
            Git.cloneRepository()
                    .setURI(repository.url)
                    .setDirectory(fileService.getDirectoryPath(*pathArray).toFile())
                    .setCredentialsProvider(getCredentialsProvider(repository))
                    .call()
        } catch (ex: GitAPIException) {
            fileService.deleteDirectoryIfExists(*pathArray)
            throw ex
        }
    }

    @FileExceptionWrap
    fun gitPull(repository: GitRepository) {
        val git = getGit(repository)
        git.pull()
                .setCredentialsProvider(getCredentialsProvider(repository))
                .call()
    }

    @FileExceptionWrap
    fun gitLog(repository: GitRepository, skip: Int, maxCount: Int): List<RevCommit> {
        val git = getGit(repository)
        return git.log()
                .setMaxCount(maxCount)
                .setSkip(skip)
                .call()
                .toList()
    }

    @FileExceptionWrap
    fun gitCommit(repository: GitRepository): CommitResult {
        val git = getGit(repository)
        val statusBeforeAdd = git.status().call()

        statusBeforeAdd.untracked.forEach { fileName ->
            git.add().addFilepattern(fileName).call()
        }
        statusBeforeAdd.modified.forEach { fileName ->
            git.add().addFilepattern(fileName).call()
        }

        val statusAfterAdd = git.status().call()
        if (statusAfterAdd.changed.isEmpty() && statusAfterAdd.added.isEmpty()) {
            return NOTHING_TO_COMMIT
        }

        val filesForCommit = HashSet<String>()
        filesForCommit.addAll(statusAfterAdd.changed)
        filesForCommit.addAll(statusAfterAdd.added)
        git.commit()
                .setAuthor(commitAuthor, commitEmail)
                .setMessage(commitMessage.format(filesForCommit))
                .call()

        val statusAfterCommit = git.status().call()
        return if (statusAfterCommit.hasUncommittedChanges()) {
            COMMITED_NOT_COMPLETELY
        } else {
            COMMITED_SUCCESSFULLY
        }
    }

    @FileExceptionWrap
    fun gitPush(repository: GitRepository) {
        val git = getGit(repository)
        git.push()
                .setCredentialsProvider(getCredentialsProvider(repository))
                .call()
    }

    private fun getGit(repository: GitRepository): Git {
        val repositoryDirectoryPath = fileService.getDirectoryPath(
                REPOS_DIRECTORY, repository.id.toString())
        return Git.open(repositoryDirectoryPath.toFile())
    }

    private fun getCredentialsProvider(repository: GitRepository): CredentialsProvider {
        return UsernamePasswordCredentialsProvider(repository.username, repository.password)
    }

}
