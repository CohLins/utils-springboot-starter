package com.colins.springutils.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnRetry {
    // 重试次数
    int retryTimes() default 3;

    // 重试异常 默认所有异常都重试
    Class<? extends Throwable> retryFor() default Exception.class;

    // 重试都失败  异常提示
    String retryOverMsg() default "系统繁忙，请稍后在试";
}
