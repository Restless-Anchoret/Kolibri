package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.BAD_REQUEST

class BadRequestException(
        message: String? = null,
        cause: Throwable? = null
) : RestApiException(BAD_REQUEST, message, cause)
