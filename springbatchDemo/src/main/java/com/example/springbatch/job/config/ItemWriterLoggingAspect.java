package com.example.springbatch.job.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description
 * @Author GoryLee
 * @Date 2024/9/14
 */
//@Aspect
//@Component
@Slf4j
public class ItemWriterLoggingAspect {

    @Before("execution(* org.springframework.batch.item.ItemWriter.write(..)) && args(items)")
    public void logBeforeWrite(JoinPoint joinPoint, List<?> items) {
        // 打印传递的对象信息
        log.info("Logging from AOP - Writing a batch of size: " + items.size());
        log.info("Logging from AOP - Writing a batch " + items);
        log.info("Items Type: " + items.getClass().getName());
    }
    @Around("execution(* org.springframework.batch.item.*.MyBatisBatchItemWriter.write(..))")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (args.length > 0) {
            log.info("Arguments passed to MyBatisBatchItemWriter.write: " + args[0]);
        }
        return joinPoint.proceed();
    }
}
