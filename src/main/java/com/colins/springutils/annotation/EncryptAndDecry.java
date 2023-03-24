package com.colins.springutils.annotation;



import com.colins.springutils.encryption.IEncryptAndDecryStrategy;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface EncryptAndDecry {

    Class<? extends IEncryptAndDecryStrategy> strategy();

    String[] dbFieldName() default {};
}
