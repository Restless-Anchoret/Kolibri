package com.ran.kolibri.component.aspect

import com.ran.kolibri.exception.FileException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import java.io.IOException

@Aspect
@Component
class FileExceptionWrapAspect {

    @Around("@annotation(com.ran.kolibri.component.aspect.annotation.FileExceptionWrap)")
    fun aroundAdvice(joinPoint: ProceedingJoinPoint) {
        try {
            joinPoint.proceed()
        } catch (ex: IOException) {
            throw FileException("IOException was occured", ex)
        }
    }

}
