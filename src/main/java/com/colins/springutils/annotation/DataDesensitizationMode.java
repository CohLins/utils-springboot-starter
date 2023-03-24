package com.colins.springutils.annotation;

import com.colins.springutils.enums.DesensitizationTypeEnum;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DataDesensitizationMode {
    DesensitizationTypeEnum typeEnum();
    int startInclude() default -1;

    int endExclude() default 0;

    String regex() default "";

    String replacedChar() default "*";
}
