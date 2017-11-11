package com.ran.kolibri.component.startup

import com.ran.kolibri.exception.FileException
import com.ran.kolibri.exception.StartUpException
import com.ran.kolibri.extension.logInfo
import com.ran.kolibri.service.file.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class AppDirectoryInitializer {

    @Autowired
    lateinit var fileService: FileService

    @EventListener
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        logInfo { "Before app directory initialization" }
        try {
            fileService.initializeAppDirectory()
        } catch (ex: FileException) {
            throw StartUpException("FileException while app directory initialization", ex)
        }
        logInfo { "After app directory initialization" }
    }

}
