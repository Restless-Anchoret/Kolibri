package com.ran.kolibri.rest

import com.ran.kolibri.service.DebugDataService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/debug-data")
@Profile("dev")
class DebugDataController {

    @Autowired
    lateinit var debugDataService: DebugDataService

    @RequestMapping(method = arrayOf(POST))
    fun populateDebugData(): ResponseEntity<Any> {
        debugDataService.populateDebugData()
        return ResponseEntity(HttpStatus.OK)
    }

}