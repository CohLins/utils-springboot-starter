package com.colins.springutils.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnRateLimiter {

    double createTokenRate() default 100; // 产生令牌的速率，默认一秒100,代表一秒的并发，理论上极限并发值为此值的两倍

    long timeOut() default 500; // 最大等待时间，默认500毫秒

    TimeUnit timeUnit() default TimeUnit.MILLISECONDS; // 等待时间单位，默认毫秒

    String timeOutMsg() default "系统繁忙，请稍后在试";
}
