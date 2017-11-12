package com.ran.kolibri.component.aspect

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

@Aspect
@Component
class LogableAspect {

    @Around("@annotation(com.ran.kolibri.component.aspect.annotation.Logable)")
    fun aroundAdvice(joinPoint: ProceedingJoinPoint): Any? {
        // todo
        return joinPoint.proceed()
    }

}
