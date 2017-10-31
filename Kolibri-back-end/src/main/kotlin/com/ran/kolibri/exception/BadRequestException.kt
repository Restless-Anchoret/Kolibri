package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.BAD_REQUEST

class BadRequestException(
        message: String? = null,
        cause: Throwable? = null
) : KolibriException(BAD_REQUEST, message, cause)
