package com.ran.kolibri.component.startup

import com.ran.kolibri.extension.logInfo
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AppDirectoryInitializer {

    @EventListener
    @Transactional
    fun handleContextRefresh(event: ContextRefreshedEvent) {
        logInfo { "Before app directory initialization" }
        // todo
        logInfo { "After app directory initialization" }
    }

}
