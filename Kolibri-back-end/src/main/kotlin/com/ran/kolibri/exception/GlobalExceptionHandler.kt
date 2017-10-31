package com.ran.kolibri.exception

import com.ran.kolibri.dto.other.ExceptionDTO
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
                               httpServletResponse: HttpServletResponse): ExceptionDTO {
        LOGGER.error(exception.message, exception)
        httpServletResponse.status = exception.httpStatus.value()
        return ExceptionDTO(exception.message)
    }

}
