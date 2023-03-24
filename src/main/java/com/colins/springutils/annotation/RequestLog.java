package com.colins.springutils.annotation;

import com.colins.springutils.enums.RequestTypeEnum;

import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLog {
    /**
     * 模块名称
     */
    String title() default "";
    /**
     * 模块描述
     */
    String describe() default "";

    /**
     * 功能类型
     */
    RequestTypeEnum businessType() default RequestTypeEnum.OTHER;

}
