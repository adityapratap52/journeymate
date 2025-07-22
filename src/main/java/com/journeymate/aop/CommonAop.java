//package com.journeymate.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StopWatch;
//
//@Aspect
//@Component
//public class CommonAop {
//    private final Logger logger = LoggerFactory.getLogger(CommonAop.class);
//
//    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
//    public void controllerPointcut() {}
//
//    @Pointcut("within(@org.springframework.stereotype.Service *)")
//    public void servicePointcut() {}
//
//    @Around("controllerPointcut() || servicePointcut()")
//    public Object logMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().getName();
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        
//        logger.info("Executing {}.{}", className, methodName);
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//
//        try {
//            Object result = joinPoint.proceed();
//            stopWatch.stop();
//            logger.info("{}.{} executed in {} ms", className, methodName, stopWatch.getTotalTimeMillis());
//            return result;
//        } catch (Exception e) {
//            logger.error("Exception in {}.{}: {}", className, methodName, e.getMessage());
//            throw e;
//        }
//    }
//
//    @Before("controllerPointcut()")
//    public void checkAuthentication() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication == null || !authentication.isAuthenticated()) {
//            logger.warn("Unauthorized access attempt detected");
//            throw new SecurityException("Authentication required");
//        }
//        logger.debug("User '{}' authenticated successfully", authentication.getName());
//    }
//
//    @AfterThrowing(pointcut = "controllerPointcut() || servicePointcut()", throwing = "exception")
//    public void logException(Exception exception) {
//        logger.error("Exception occurred: {}", exception.getMessage());
//        logger.error("Stack trace: ", exception);
//    }
//}
