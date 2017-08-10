package com.ran.kolibri.rest

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/ping")
class PingController {

    @RequestMapping(method = arrayOf(GET))
    fun ping(): ResponseEntity<Any> {
        return ResponseEntity(HttpStatus.OK)
    }

}