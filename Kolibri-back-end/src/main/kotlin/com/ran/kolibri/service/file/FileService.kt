package com.ran.kolibri.service.file

import com.ran.kolibri.app.Config.APP_DIRECTORY_ROOT
import com.ran.kolibri.component.aspect.annotation.FileExceptionWrap
import com.ran.kolibri.exception.FileException
import com.ran.kolibri.extension.logError
import com.ran.kolibri.extension.logInfo
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.io.PrintWriter
import java.nio.file.Files.*
import java.nio.file.Path
import java.nio.file.Paths

@Service
class FileService {

    companion object {
        val REPOS_DIRECTORY = "repos"

        private val DEFAULT_APP_DIRECTORY_ROOT_PREFIX = ".Kolibri"
        private val APP_DIRECTORY_SUBDIRECTORIES_NAMES = listOf(REPOS_DIRECTORY)

        private val REPOSITORY_DIRECTORY_NAME_FORMAT = "repository-%s"
        private val EXPORTED_PROJECT_FILE_NAME_FORMAT = "project-%s"
    }

    @Value(APP_DIRECTORY_ROOT)
    private lateinit var appDirectoryRoot: String

    private val actualAppDirectoryRoot: String by lazy {
        if (appDirectoryRoot.isEmpty()) {
            "${System.getProperty("user.home")}/$DEFAULT_APP_DIRECTORY_ROOT_PREFIX"
        } else {
            appDirectoryRoot
        }
    }

    @FileExceptionWrap
    fun initializeAppDirectory() {
        val appDirectoryPath = getDirectoryPath()
        if (exists(appDirectoryPath) && !isDirectory(appDirectoryPath)) {
            logError { "App directory root path is not a directory" }
            throw FileException("App directory root path is not a directory")
        }
        createDirectoryIfNotExists()
        APP_DIRECTORY_SUBDIRECTORIES_NAMES.forEach { subdirectoryName ->
            createDirectoryIfNotExists(subdirectoryName)
        }
    }

    @FileExceptionWrap
    fun createDirectoryIfNotExists(vararg pathDirectories: String) {
        val directoryPath = getDirectoryPath(*pathDirectories)
        if (notExists(directoryPath)) {
            if (!pathDirectories.isEmpty()) {
                val pathDirectoriesSlice = pathDirectories.asList().subList(0, pathDirectories.size - 1).toTypedArray()
                createDirectoryIfNotExists(*pathDirectoriesSlice)
            }
            logInfo { "Directory $directoryPath does not exist, creating it" }
            createDirectory(directoryPath)
        }
    }

    @FileExceptionWrap
    fun deleteDirectoryIfExists(vararg pathDirectories: String) {
        val directoryPath = getDirectoryPath(*pathDirectories)
        if (exists(directoryPath) && isDirectory(directoryPath)) {
            delete(directoryPath)
        }
    }

    @FileExceptionWrap
    fun writeToFile(content: String, fileName: String, vararg pathDirectories: String) {
        createDirectoryIfNotExists(*pathDirectories)
        val filePath = getFilePath(fileName, *pathDirectories)
        if (notExists(filePath)) {
            createFile(filePath)
        }
        PrintWriter(filePath.toString()).use { writer ->
            writer.println(content)
        }
    }

    fun getDirectoryPath(vararg pathDirectories: String): Path {
        return Paths.get(actualAppDirectoryRoot, *pathDirectories)
    }

    fun getFilePath(fileName: String, vararg pathDirectories: String): Path {
        val directoryPath = getDirectoryPath(*pathDirectories)
        return directoryPath.resolve(fileName)
    }

    fun getRepositoryDirectoryName(repositoryId: Long): String {
        return REPOSITORY_DIRECTORY_NAME_FORMAT.format(repositoryId)
    }

    fun getExportedProjectFileName(projectId: Long): String {
        return EXPORTED_PROJECT_FILE_NAME_FORMAT.format(projectId)
    }

}
