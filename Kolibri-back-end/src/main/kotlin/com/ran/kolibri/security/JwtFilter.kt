package com.ran.kolibri.security

import com.ran.kolibri.exception.GlobalExceptionHandler
import com.ran.kolibri.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Instant
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : Filter {

    companion object {
        private val LOGIN_END_POINT_URL = "/users/login"
    }

    @Autowired
    lateinit var jwtGenerator: JwtGenerator

    override fun init(filterConfig: FilterConfig) { }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        if (httpServletRequest.requestURI == LOGIN_END_POINT_URL) {
            chain.doFilter(request, response)
            return
        }

        val decodeResult = jwtGenerator.decodeJwt(httpServletRequest)
        if (!decodeResult.isSuccess) {
            GlobalExceptionHandler.handleKolibriExceptionGlobal(
                    UnauthorizedException("Valid JWT token must be provided"),
                    httpServletResponse)
            return
        }

        if (decodeResult.refresh.isBefore(Instant.now())) {
            jwtGenerator.encodeJwt(decodeResult.userData, httpServletResponse)
        }

        SecurityContextHolder.getContext().authentication = JwtAuthentication(decodeResult.userData)
        chain.doFilter(request, response)
    }

    override fun destroy() { }

}
