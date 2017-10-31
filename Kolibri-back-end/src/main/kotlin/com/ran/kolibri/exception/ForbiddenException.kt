package com.ran.kolibri.exception

import org.springframework.http.HttpStatus.FORBIDDEN

class ForbiddenException(
        message: String? = null,
        cause: Throwable? = null
) : KolibriException(FORBIDDEN, message, cause)
