package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR

class InternalServerErrorException(
        message: String? = null,
        cause: Throwable? = null
) : RestApiException(INTERNAL_SERVER_ERROR, message, cause)
