package com.ran.kolibri.exception

class StartUpException(
        message: String? = null,
        cause: Throwable? = null
) : RuntimeException(message, cause)
