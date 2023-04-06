package com.colins.springutils.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnHystrix {

    int cpuLimit() default 95;

    int heapMemoryLimit() default 90;

    int errorNumLimit() default -1;

    int errorRatioLimit() default -1;

    int windowTimeInMs() default 1000;

    int windowCount() default 2;

    String hystrixMsg() default "系统繁忙，请稍后再试";
}
