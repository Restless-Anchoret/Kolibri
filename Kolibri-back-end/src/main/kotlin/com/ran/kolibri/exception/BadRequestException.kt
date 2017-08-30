package com.ran.kolibri.exception

import java.lang.RuntimeException

class BadRequestException(
        message: String? = null,
        cause: Throwable? = null
) : RuntimeException(message, cause)
