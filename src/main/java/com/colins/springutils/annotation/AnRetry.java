package com.colins.springutils.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnRetry {

    int retryTimes() default 3;

    Class<? extends Throwable> retryFor() default Exception.class;

    String retryOverMsg() default "系统繁忙，请稍后在试";
}
