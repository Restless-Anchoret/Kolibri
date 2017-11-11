package com.ran.kolibri.exception

import org.springframework.http.HttpStatus

abstract class RestApiException(
        val httpStatus: HttpStatus,
        message: String? = null,
        cause: Throwable? = null
) : RuntimeException(message, cause)
