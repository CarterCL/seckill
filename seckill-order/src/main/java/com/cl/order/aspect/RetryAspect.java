package com.cl.order.aspect;

import com.cl.base.annotation.Retry;
import com.cl.base.enums.CodeEnum;
import com.cl.base.exception.GlobalException;
import com.cl.base.exception.RetryException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author CarterCL
 * @create 2020/10/5 22:42
 */
@Slf4j
@Component
@Aspect
public class RetryAspect {
    @Pointcut("@annotation(com.cl.base.annotation.Retry)")
    public void execute() {
    }

    @Around("execute() && @annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        
        int retryTimes = 0;
        do {
            try {
                retryTimes++;
                return joinPoint.proceed();
            } catch (RetryException exception) {
                log.warn("重试次数:{}", retryTimes);
                if (retry.duration() > 0) {
                    TimeUnit.MILLISECONDS.sleep(retry.duration());
                }
            }
        } while (retryTimes <= retry.maxTimes());
        log.error("重试失败");
        throw new GlobalException(CodeEnum.SYSTEM_ERROR);
    }
}
