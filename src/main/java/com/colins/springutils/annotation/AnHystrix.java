package com.colins.springutils.annotation;

import java.lang.annotation.*;

@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnHystrix {
    // CPU上限  超过则熔断降级
    int cpuLimit() default 95;
    // 内存上限  超过则熔断降级
    int heapMemoryLimit() default 90;
    // 异常数上限  超过则熔断降级 -1默认不开启
    int errorNumLimit() default -1;
    // 异常率上限  超过则熔断降级 -1默认不开启
    int errorRatioLimit() default -1;
    // 窗口总大小  单位：毫秒
    int windowTimeInMs() default 1000;
    // 窗口数量
    int windowCount() default 2;
    // 降级返回信息
    String hystrixMsg() default "系统繁忙，请稍后再试";
}
