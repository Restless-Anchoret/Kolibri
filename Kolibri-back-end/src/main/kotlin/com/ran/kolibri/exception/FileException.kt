package com.ran.kolibri.exception

class FileException(
        message: String? = null,
        cause: Throwable? = null
) : InternalServerErrorException(message, cause)
