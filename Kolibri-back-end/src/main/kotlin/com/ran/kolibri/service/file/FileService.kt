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
import java.nio.file.Paths

@Service
class FileService {

    companion object {
        val REPOS_DIRECTORY = "repos"

        private val DEFAULT_APP_DIRECTORY_ROOT_PREFIX = ".Kolibri"
        private val APP_DIRECTORY_SUBDIRECTORIES_NAMES = listOf(REPOS_DIRECTORY)
    }

    @Value(APP_DIRECTORY_ROOT)
    lateinit var appDirectoryRoot: String

    val actualAppDirectoryRoot: String by lazy {
        if (appDirectoryRoot.isEmpty()) {
            "${System.getProperty("user.home")}/$DEFAULT_APP_DIRECTORY_ROOT_PREFIX"
        } else {
            appDirectoryRoot
        }
    }

    @FileExceptionWrap
    fun initializeAppDirectory() {
        val appDirectoryPath = Paths.get(actualAppDirectoryRoot)
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
        val directoryPath = Paths.get(actualAppDirectoryRoot, *pathDirectories)
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
    fun writeToFile(content: String, fileName: String, vararg pathDirectories: String) {
        createDirectoryIfNotExists(*pathDirectories)
        val directoryPath = Paths.get(actualAppDirectoryRoot, *pathDirectories)
        val filePath = directoryPath.resolve(fileName)
        if (notExists(filePath)) {
            createFile(filePath)
        }
        PrintWriter(filePath.toString()).use { writer ->
            writer.println(content)
        }
    }

}
