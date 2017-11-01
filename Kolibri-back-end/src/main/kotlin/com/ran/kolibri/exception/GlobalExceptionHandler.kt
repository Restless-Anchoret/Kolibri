package com.ran.kolibri.exception

import com.ran.kolibri.dto.other.ExceptionDto
import org.apache.log4j.Logger
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        private val LOGGER = Logger.getLogger(GlobalExceptionHandler::class.java)
    }

    @ExceptionHandler(KolibriException::class)
    @ResponseBody
    fun handleKolibriException(exception: KolibriException,
                               httpServletResponse: HttpServletResponse): ExceptionDto {
        LOGGER.error(exception.message, exception)
        httpServletResponse.status = exception.httpStatus.value()
        return ExceptionDto(exception.message, exception.httpStatus.value())
    }

}
