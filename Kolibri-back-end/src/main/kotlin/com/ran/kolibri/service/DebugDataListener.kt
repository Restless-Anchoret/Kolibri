package com.ran.kolibri.service

import com.ran.kolibri.entity.property.GlobalProperty
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.repository.property.GlobalPropertyRepository
import org.apache.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class DebugDataListener : ApplicationRunner {

    companion object {
        private val LOGGER = Logger.getLogger(DebugDataListener::class.java)
        private val DEBUG_DATA_PROPERTY_KEY = "debug.data"
        private val DEBUG_DATA_PROPERTY_VALUE = "populated"
    }

    @Autowired
    lateinit var globalPropertyRepository: GlobalPropertyRepository

    @Autowired
    lateinit var projectRepository: ProjectRepository

    @Transactional
    override fun run(args: ApplicationArguments?) {
        LOGGER.info("Before debug data population")

        val debugDataProperty = globalPropertyRepository.findByKey(DEBUG_DATA_PROPERTY_KEY)
        if (debugDataProperty != null) {
            LOGGER.info("Debug data has been already populated")
            return
        }

        val createdDebugDataProperty = GlobalProperty()
        createdDebugDataProperty.key = DEBUG_DATA_PROPERTY_KEY
        createdDebugDataProperty.value = DEBUG_DATA_PROPERTY_VALUE
        globalPropertyRepository.save(createdDebugDataProperty)

        LOGGER.info("After debug data population")
    }

}