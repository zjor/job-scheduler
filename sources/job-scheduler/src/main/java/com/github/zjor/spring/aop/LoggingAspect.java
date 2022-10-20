package com.github.zjor.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Around("@annotation(Log)")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = signature.getParameterNames();
        Object[] paramValues = joinPoint.getArgs();

        StringBuilder args = new StringBuilder();
        for (int i = 0; i < paramNames.length; i++) {
            args.append(paramNames[i]).append('=').append(paramValues[i]).append(',').append(' ');
        }
        if (args.length() > 2) {
            args.setLength(args.length() - 2);
        }
        try {
            Object result = joinPoint.proceed();

            //TODO: log result or nothing if Void
            log.info("{}.{}({})", signature.getDeclaringType().getSimpleName(), signature.getName(), args);

            return result;
        } catch (Throwable t) {
            log.info("{}.{}({}) threw {}", signature.getDeclaringType().getSimpleName(),
                    signature.getName(), args, t.toString());

            throw t;
        }
    }

}
