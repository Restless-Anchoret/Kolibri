package com.ran.kolibri.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.ran.kolibri.dto.exception.ExceptionDto
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val LOGGER = Logger.getLogger(GlobalExceptionHandler::class.java)

        fun handleKolibriExceptionGlobal(exception: KolibriException,
                                         httpServletResponse: HttpServletResponse) {
            LOGGER.error(exception.message, exception)
            val exceptionDto = ExceptionDto(exception.message, exception.httpStatus.value())
            val message = ObjectMapper().writeValueAsString(exceptionDto)
            httpServletResponse.writer.print(message)
            httpServletResponse.contentType = "application/json"
            httpServletResponse.status = exception.httpStatus.value()
        }
    }

    @ExceptionHandler(KolibriException::class)
    fun handleKolibriException(exception: KolibriException,
                               httpServletResponse: HttpServletResponse) {
        handleKolibriExceptionGlobal(exception, httpServletResponse)
    }

}
