package com.ran.kolibri.service.file

import com.ran.kolibri.app.Config.APP_DIRECTORY_ROOT
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class FileService {

    companion object {
        private val DEFAULT_APP_DIRECTORY_ROOT_PREFIX = ".Kolibri"
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

}
