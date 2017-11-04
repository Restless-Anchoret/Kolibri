package com.ran.kolibri.security

import com.ran.kolibri.exception.ForbiddenException
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.security.annotation.ProjectId
import com.ran.kolibri.service.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
class ProjectProtectedAdvice {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var projectRepository: ProjectRepository

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    @Transactional
    fun aroundAdvice(joinPoint: ProceedingJoinPoint): Any {
        val method = (joinPoint.signature as MethodSignature).method
        var parameterIndex = -1
        method.parameters.forEachIndexed { index, parameter ->
            if (parameter.isAnnotationPresent(ProjectId::class.java)) {
                parameterIndex = index
            }
        }

        if (parameterIndex >= 0) {
            val projectId = joinPoint.args[parameterIndex] as Long
            val authenticatedUserId = userService.getAuthenticatedUserData().id

            val project = projectRepository.findOne(projectId)
            if (project != null && project.owner?.id != authenticatedUserId &&
                    project.usersWithAccess.none { it.id == authenticatedUserId }) {
                throw ForbiddenException("Current User has not access to this")
            }
        }

        return joinPoint.proceed()
    }

}
