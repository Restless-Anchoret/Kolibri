package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.UNAUTHORIZED

class UnauthorizedException(
        message: String? = null,
        cause: Throwable? = null
) : KolibriException(UNAUTHORIZED, message, cause)
