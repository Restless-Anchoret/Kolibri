package com.ran.kolibri.security

import com.ran.kolibri.entity.user.UserRole
import com.ran.kolibri.exception.ForbiddenException
import com.ran.kolibri.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Aspect
@Component
class AdminProtectedAdvice {

    @Autowired
    lateinit var userService: UserService

    @Around("@annotation(com.ran.kolibri.security.annotation.AdminProtected)")
    fun aroundAdvice(joinPoint: ProceedingJoinPoint): Any {
        val userData = userService.getAuthenticatedUserData()
        if (userData.userRole != UserRole.ADMIN) {
            throw ForbiddenException("Admin role is required")
        }
        return joinPoint.proceed()
    }

}
