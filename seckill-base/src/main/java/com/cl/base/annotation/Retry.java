package com.cl.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author CarterCL
 * @create 2020/10/5 22:34
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Retry {
    /**
     * 重试次数
     * @return 重试次数
     */
    int maxTimes() default 3;

    /**
     * 重试间隔时间(单位 ms)
     * @return 重试间隔时间
     */
    long duration() default 0;

    /**
     * 失败异常信息
     * @return 失败异常信息
     */
    String failedMessage() default "";
}
