package com.ran.kolibri.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.ran.kolibri.dto.exception.ExceptionDto
import com.ran.kolibri.extension.logError
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import javax.servlet.http.HttpServletResponse

@ControllerAdvice
class GlobalExceptionHandler {

    companion object {
        fun handleKolibriExceptionGlobal(exception: RestApiException,
                                         httpServletResponse: HttpServletResponse) {
            logError(exception) { exception.message!! }
            val exceptionDto = ExceptionDto(exception.message, exception.httpStatus.value())
            val message = ObjectMapper().writeValueAsString(exceptionDto)
            httpServletResponse.writer.print(message)
            httpServletResponse.contentType = "application/json"
            httpServletResponse.status = exception.httpStatus.value()
        }
    }

    @ExceptionHandler(RestApiException::class)
    fun handleKolibriException(exception: RestApiException,
                               httpServletResponse: HttpServletResponse) {
        handleKolibriExceptionGlobal(exception, httpServletResponse)
    }

}
