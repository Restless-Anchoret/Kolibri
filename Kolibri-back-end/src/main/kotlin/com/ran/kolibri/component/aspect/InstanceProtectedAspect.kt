package com.ran.kolibri.component.aspect

import com.ran.kolibri.exception.ForbiddenException
import com.ran.kolibri.repository.project.ProjectRepository
import com.ran.kolibri.repository.vcs.GitRepositoryRepository
import com.ran.kolibri.component.aspect.annotation.ProjectId
import com.ran.kolibri.component.aspect.annotation.RepositoryId
import com.ran.kolibri.service.user.UserService
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Aspect
@Component
class InstanceProtectedAspect {

    @Autowired
    lateinit var userService: UserService
    @Autowired
    lateinit var projectRepository: ProjectRepository
    @Autowired
    lateinit var gitRepositoryRepository: GitRepositoryRepository

    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    @Transactional
    fun aroundAdvice(joinPoint: ProceedingJoinPoint): Any? {
        val projectId = extractIdByAnnotation(joinPoint, ProjectId::class.java)
        if (projectId != null) {
            val authenticatedUserId = userService.getAuthenticatedUserData().id
            val project = projectRepository.findOne(projectId)
            if (project != null && project.owner?.id != authenticatedUserId &&
                    project.usersWithAccess.none { it.id == authenticatedUserId }) {
                throw ForbiddenException("Current User has not access to this Project")
            }
        }

        val repositoryId = extractIdByAnnotation(joinPoint, RepositoryId::class.java)
        if (repositoryId != null) {
            val authenticatedUserId = userService.getAuthenticatedUserData().id
            val repository = gitRepositoryRepository.findOne(repositoryId)
            if (repository != null && repository.owner?.id != authenticatedUserId) {
                throw ForbiddenException("Current User has not access to this Git Repository")
            }
        }

        return joinPoint.proceed()
    }

    private fun <T : Annotation> extractIdByAnnotation(joinPoint: ProceedingJoinPoint,
                                                       annotationClass: Class<T>): Long? {
        val method = (joinPoint.signature as MethodSignature).method
        var parameterIndex = -1
        method.parameters.forEachIndexed { index, parameter ->
            if (parameter.isAnnotationPresent(annotationClass)) {
                parameterIndex = index
            }
        }
        return if (parameterIndex >= 0) {
            joinPoint.args[parameterIndex] as Long
        } else {
            null
        }
    }

}
