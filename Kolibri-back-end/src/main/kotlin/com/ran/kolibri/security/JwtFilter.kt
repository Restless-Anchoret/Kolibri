package com.ran.kolibri.security

import com.ran.kolibri.app.Config.JWT_IGNORED_URLS
import com.ran.kolibri.exception.GlobalExceptionHandler
import com.ran.kolibri.exception.UnauthorizedException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.stereotype.Component
import java.time.Instant
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtFilter : Filter {

    @Autowired
    lateinit var jwtGenerator: JwtGenerator
    @Value(JWT_IGNORED_URLS)
    lateinit var jwtIgnoredUrls: Array<String>

    private val ignoredUrlMatchers: List<AntPathRequestMatcher> by lazy {
        jwtIgnoredUrls.map { url -> AntPathRequestMatcher(url) }
    }

    override fun init(filterConfig: FilterConfig) { }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val httpServletResponse = response as HttpServletResponse

        if (ignoredUrlMatchers.any { it.matches(request) }) {
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
