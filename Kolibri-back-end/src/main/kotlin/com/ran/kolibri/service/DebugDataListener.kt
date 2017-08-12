package com.ran.kolibri.service

import org.apache.log4j.Logger
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

@Component
class DebugDataListener : ApplicationListener<ApplicationReadyEvent> {

    companion object {
        private val LOGGER = Logger.getLogger(DebugDataListener::class.java)
    }

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        LOGGER.info("Before debug data population")

        LOGGER.info("After debug data population")
    }

}