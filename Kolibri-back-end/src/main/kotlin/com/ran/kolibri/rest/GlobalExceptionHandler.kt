package com.ran.kolibri.rest

import com.ran.kolibri.dto.other.ExceptionDTO
import com.ran.kolibri.exception.BadRequestException
import com.ran.kolibri.exception.InternalServerErrorException
import com.ran.kolibri.exception.NotFoundException
import org.apache.log4j.Logger
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val LOGGER = Logger.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(NOT_FOUND)
    @ResponseBody
    fun handleNotFoundException(exception: NotFoundException): ExceptionDTO {
        return handleException(exception)
    }

    @ExceptionHandler(BadRequestException::class)
    @ResponseStatus(BAD_REQUEST)
    @ResponseBody
    fun handleBadRequestException(exception: BadRequestException): ExceptionDTO {
        return handleException(exception)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ResponseBody
    fun handleInternalServerErrorException(exception: InternalServerErrorException): ExceptionDTO {
        return handleException(exception)
    }

    private fun handleException(exception: Exception): ExceptionDTO {
        LOGGER.error(exception.message, exception)
        return ExceptionDTO(exception.message)
    }

}
