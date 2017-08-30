package com.ran.kolibri.exception

import java.lang.RuntimeException

class NotFoundException(
        message: String? = null,
        cause: Throwable? = null
) : RuntimeException(message, cause)
