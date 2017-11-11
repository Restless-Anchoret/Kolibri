package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.NOT_FOUND

class NotFoundException(
        message: String? = null,
        cause: Throwable? = null
) : RestApiException(NOT_FOUND, message, cause)
