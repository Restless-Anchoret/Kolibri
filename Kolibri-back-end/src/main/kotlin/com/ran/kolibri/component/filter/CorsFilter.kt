package com.ran.kolibri.component.filter

import org.springframework.stereotype.Component
import javax.servlet.*
import javax.servlet.http.HttpServletResponse

@Component
class CorsFilter : Filter {

    override fun init(filterConfig: FilterConfig) { }

    override fun doFilter(request: ServletRequest, response: ServletResponse, filterChain: FilterChain) {
        response as HttpServletResponse
        response.setHeader("Access-Control-Allow-Origin", "*")
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
        response.setHeader("Access-Control-Max-Age", "86400")
        response.setHeader("Access-Control-Allow-Headers", "Content-Type")
        filterChain.doFilter(request, response)
    }

    override fun destroy() { }

}
