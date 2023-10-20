package com.fairfair.callcenter.api.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Romain Lavabre <romainlavabre98@gmail.com>
 */
@Aspect
@Component
public class ReadOnlyInterceptor {


    @Around( "@annotation(com.fairfair.callcenter.api.datasource.ReadOnly)" )
    public Object aroundMethod( ProceedingJoinPoint joinPoint ) throws Throwable {

        try {
            ReadOnlyResolver.getInstance().setReadOnly();
            return joinPoint.proceed();
        } finally {
            ReadOnlyResolver.getInstance().clear();
        }
    }
}
