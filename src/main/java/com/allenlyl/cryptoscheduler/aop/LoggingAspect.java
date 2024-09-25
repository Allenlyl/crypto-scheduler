package com.allenlyl.cryptoscheduler.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {
   @Around("@annotation(com.allenlyl.cryptoscheduler.annotation.LogExecutionTime)")
   public Object LogExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
      System.out.println(pjp.toString() + " start");
      long start = System.currentTimeMillis();
      Object result = pjp.proceed();
      System.out.println(pjp.toString() + " finish");
      long time = System.currentTimeMillis() - start;
      System.out.println(pjp.getSignature() + " executed in " + time + " ms.");
      return result;
   }
}
